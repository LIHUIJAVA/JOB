package com.px.mis.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvResponse;

public class JacksonUtil {

	private static ObjectMapper mapper;
	private static ObjectMapper mapper1 = configMapper1();// 启用注解
	private static ObjectMapper mapper2 = configMapper2();// 禁用注解
	static {// 初始化时候先关闭注解
		turnOffAnno();
	}

	public static void turnOffAnno() {
		mapper = mapper2;
	}

	public static void turnOnAnno() {
		mapper = mapper1;
	}

	// 使得Json注解序列化失效的重写子类
	@SuppressWarnings("serial")
	public static class NonFieldSerializer extends JacksonAnnotationIntrospector implements Versioned {
		@Override
		public PropertyName findNameForSerialization(Annotated a) {
			JsonGetter jg = _findAnnotation(a, JsonGetter.class);
			if (jg != null) {
				return PropertyName.construct(jg.value());
			}
			JsonProperty pann = _findAnnotation(a, JsonProperty.class);

			return null;
		}
	}

	// POJO转ObjectNode
	public static ObjectNode getObjectNode(Object object) throws IOException {
		return (ObjectNode) mapper.readTree(mapper.writeValueAsString(object));
	}

	// String转ObjectNode
	public static ObjectNode getObjectNode(String jsonStr) throws IOException {
		if (jsonStr == null || "".equals(jsonStr)) {
			jsonStr = "{}";
		}
		return (ObjectNode) mapper.readTree(jsonStr);
	}

	// List转ObjectNode-----已过时
	@Deprecated
	public static ObjectNode getObjectNode(List list) throws IOException {
		return (ObjectNode) mapper.readTree("{\"list\":" + mapper.writeValueAsString(list) + "}");
	}

	// List转ArrayNode
	public static ArrayNode getArrayNode(List list) throws IOException {
		if (list != null) {
			return (ArrayNode) mapper.readTree(mapper.writeValueAsString(list));
		}
		return (ArrayNode) mapper.readTree("[]");
	}

	// 递归实现,getArrayNode,贼牛逼
	public static ArrayNode getArrayNode(JsonNode readTree) throws IOException {
		// 拆分这个JsonNode
		ArrayNode dArrayNode = null;
		Iterator<Entry<String, JsonNode>> fields = readTree.fields();
		// 遍历拆开后的
		while (fields.hasNext()) {
			JsonNode value = fields.next().getValue();// 取得每一个子节点ValueNode
			String str = value.toString();
			if (str.indexOf('[') != -1) {// 包含
				// 进入递归
				if (value.isArray()) {
					// 是数组时候,返回
					return (ArrayNode) value;
				} else {
					// 包含,但不是数组,说明是父元素,继续拆分
					dArrayNode = getArrayNode(value);
				}
			}
			// 不包含,去下一个子元素
			continue;
		}
		// 循环完毕还是没有返回值
		return dArrayNode;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getPOJOList(String jsonStr, Class<T> objClass) throws IOException {
		JavaType javaType = getCollectionType(ArrayList.class, objClass);
		return (List<T>) mapper.readValue(jsonStr, javaType);

	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses  元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	// Map转ObjectNode
	public static ObjectNode getObjectNode(Map map) throws IOException {
		if (null == map) {
			return JacksonUtil.getObjectNode("");
		}
		return (ObjectNode) mapper.readTree(mapper.writeValueAsString(map));
	}

	// ObjectNode转POJO
	public static <T> T getPOJO(ObjectNode objectNode, Class<T> objClass) throws IOException {
		return mapper.readValue(objectNode.toString(), objClass);
	}

	// ObjectNode转JsonString

	// ObjectNode转List
	public static List<Map> getList(String jsonStr) throws IOException {
		return mapper.readValue(jsonStr, ArrayList.class);
	}

	// ObjectNode转Map
	public static Map getMap(String jsonStr) throws IOException {
		Map map = mapper.readValue(jsonStr, HashMap.class);
		CommonUtil.mapTrimToNull(map);
		return map;
	}

	/********************** 以下为序列化配置部分 *****************************/

	// mapper2的配置
	public static ObjectMapper configMapper2() {// 禁用注解

		mapper2 = new ObjectMapper().setAnnotationIntrospector(new NonFieldSerializer())// 所有的JsonProperties不起作用
				.enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN)// 禁用科学记数法
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)// 防止json中不存在的字段,反序列化报错
				.disable(Feature.WRITE_NUMBERS_AS_STRINGS);// 禁用数字写为字符串
					
		mapper2.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeString("");
			}
		});
		mapper2.configure(MapperFeature.USE_ANNOTATIONS, false);
		SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper2.setDateFormat(smt);
		mapper2.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 解决时区差8小时问题
		return mapper2;

	}

	// mapper1的配置
	@SuppressWarnings("serial")
	public static ObjectMapper configMapper1() {// 启用注解

		mapper1 = new ObjectMapper().setAnnotationIntrospector(new NonFieldSerializer())// 所有的JsonProperties不起作用
				.enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN)// 禁用科学记数法
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)// 防止json中不存在的字段,反序列化报错
				.disable(Feature.WRITE_NUMBERS_AS_STRINGS);// 禁用数字写为字符串
		mapper1.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeString("");
			}
		});
		// 设置全局的时间转化
		SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper1.setDateFormat(smt);
		mapper1.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 解决时区差8小时问题
		// 此处启用注解
		mapper1.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

			@SuppressWarnings("unchecked")
			private Class<? extends Annotation>[] ANNOTATIONS_TO_INFER_SER = (Class<? extends Annotation>[]) new Class<?>[] {
					JsonSerialize.class, JsonView.class, JsonFormat.class, JsonTypeInfo.class, JsonRawValue.class,
					JsonUnwrapped.class, JsonBackReference.class, JsonManagedReference.class };

			@Override
			public Version version() {
				// TODO Auto-generated method stub
				return null;
			}

			// 注解替换JsonKey值
			@Override
			public PropertyName findNameForSerialization(Annotated a) {
				JsonGetter jg = _findAnnotation(a, JsonGetter.class);
				if (jg != null) {
					return PropertyName.construct(jg.value());
				}
				JsonProperty pann = _findAnnotation(a, JsonProperty.class);
				if (pann != null) {
					return PropertyName.construct(pann.value());
				}
				if (_hasOneOf(a, ANNOTATIONS_TO_INFER_SER)) {
					return PropertyName.USE_DEFAULT;
				}
				return null;
			}
		});
		return mapper1;
	}

	/**********
	 * 代替fastjson
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 **************/
	// 获取json格式字符串
	public static String serialize(Object object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);

	}

	/*** xml 工具类 ***/
	public static String getXmlStr(Object o) throws JsonProcessingException {

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN,true);
		xmlMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeString("");
			}
		});
		
		return xmlMapper.writeValueAsString(o);

	}

	public static <T> MappingIterator<T> getResponse(String superBeanName,Class<T> cl,String xmlStr)
			throws IOException, JsonProcessingException {
		XmlMapper xmlMapper = new XmlMapper();
		String s = xmlMapper.readTree(xmlStr).get(superBeanName).asText();
		MappingIterator<T> iterator = xmlMapper.readerFor(cl)
				.readValues(s);
		return iterator;
	}

}
