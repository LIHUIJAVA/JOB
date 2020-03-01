package com.px.mis.ec.util;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	private static String JOB_NAME = "QuartzJob";
	private static String TRIGGER_NAME = "QuartzTrigger";

	/**
	 * �������
	 * @param groupName 
	 * @param time Сʱ
	 */
	public static void addJob(String groupName, int time) {
		try {
			// ͨ��SchedulerFactory����Scheduler����
			Scheduler scheduler = schedulerFactory.getScheduler();

			// ��������Jobʵ���༰������һЩ��̬��Ϣ������һ����ҵʵ��
			JobDetail jobDetail = JobBuilder.newJob(DownloadJob.class).withIdentity(JOB_NAME, groupName)// ����ҵ��һ�����ֺ�����
					.build();

			// ����һ�����������涨�����Ĺ���
			SimpleTrigger trigger = TriggerBuilder// ����һ���µ�TriggerBuilder���淶һ��������
					.newTrigger()// ����������һ�����ֺ�����
					.withIdentity(TRIGGER_NAME, groupName)// ����ִ��
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(time))
					.build();// ����������

			// ��Scheduler�����job�����trigger������
			scheduler.scheduleJob(jobDetail, trigger);
			if (!scheduler.isShutdown()) {
				// ����
				scheduler.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �޸�����
	 * @param groupName
	 * @param time Сʱ
	 */
	public static void modifyJobTime(String groupName, int time) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_NAME, groupName);

			SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				// ������ʱ����
				addJob(groupName, time);
				return;
			}
			// ������
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// ��������,��������
			triggerBuilder.withIdentity(TRIGGER_NAME, groupName);
			// ������ʱ���趨
			triggerBuilder
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(time));
			// ����Trigger����
			trigger = (SimpleTrigger) triggerBuilder.build();
			// �޸�һ������Ĵ���ʱ��
			scheduler.rescheduleJob(triggerKey, trigger);
			/** ���� rescheduleJob ���� */
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
