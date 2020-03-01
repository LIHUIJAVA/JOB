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
	 * 添加任务
	 * @param groupName 
	 * @param time 小时
	 */
	public static void addJob(String groupName, int time) {
		try {
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler scheduler = schedulerFactory.getScheduler();

			// 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
			JobDetail jobDetail = JobBuilder.newJob(DownloadJob.class).withIdentity(JOB_NAME, groupName)// 给作业起一个名字和组名
					.build();

			// 构建一个触发器，规定触发的规则
			SimpleTrigger trigger = TriggerBuilder// 创建一个新的TriggerBuilder来规范一个触发器
					.newTrigger()// 给触发器起一个名字和组名
					.withIdentity(TRIGGER_NAME, groupName)// 立即执行
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(time))
					.build();// 产生触发器

			// 向Scheduler中添加job任务和trigger触发器
			scheduler.scheduleJob(jobDetail, trigger);
			if (!scheduler.isShutdown()) {
				// 启动
				scheduler.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改任务
	 * @param groupName
	 * @param time 小时
	 */
	public static void modifyJobTime(String groupName, int time) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_NAME, groupName);

			SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				// 创建定时任务
				addJob(groupName, time);
				return;
			}
			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity(TRIGGER_NAME, groupName);
			// 触发器时间设定
			triggerBuilder
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(time));
			// 创建Trigger对象
			trigger = (SimpleTrigger) triggerBuilder.build();
			// 修改一个任务的触发时间
			scheduler.rescheduleJob(triggerKey, trigger);
			/** 调用 rescheduleJob 结束 */
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
