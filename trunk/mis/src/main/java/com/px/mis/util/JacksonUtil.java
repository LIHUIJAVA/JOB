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
	private static ObjectMapper mapper1 = configMapper1();// ����ע��
	private static ObjectMapper mapper2 = configMapper2();// ����ע��
	static {// ��ʼ��ʱ���ȹر�ע��
		turnOffAnno();
	}

	public static void turnOffAnno() {
		mapper = mapper2;
	}

	public static void turnOnAnno() {
		mapper = mapper1;
	}

	// ʹ��Jsonע�����л�ʧЧ����д����
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

	// POJOתObjectNode
	public static ObjectNode getObjectNode(Object object) throws IOException {
		return (ObjectNode) mapper.readTree(mapper.writeValueAsString(object));
	}

	// StringתObjectNode
	public static ObjectNode getObjectNode(String jsonStr) throws IOException {
		if (jsonStr == null || "".equals(jsonStr)) {
			jsonStr = "{}";
		}
		return (ObjectNode) mapper.readTree(jsonStr);
	}

	// ListתObjectNode-----�ѹ�ʱ
	@Deprecated
	public static ObjectNode getObjectNode(List list) throws IOException {
		return (ObjectNode) mapper.readTree("{\"list\":" + mapper.writeValueAsString(list) + "}");
	}

	// ListתArrayNode
	public static ArrayNode getArrayNode(List list) throws IOException {
		if (list != null) {
			return (ArrayNode) mapper.readTree(mapper.writeValueAsString(list));
		}
		return (ArrayNode) mapper.readTree("[]");
	}

	// �ݹ�ʵ��,getArrayNode,��ţ��
	public static ArrayNode getArrayNode(JsonNode readTree) throws IOException {
		// ������JsonNode
		ArrayNode dArrayNode = null;
		Iterator<Entry<String, JsonNode>> fields = readTree.fields();
		// �����𿪺��
		while (fields.hasNext()) {
			JsonNode value = fields.next().getValue();// ȡ��ÿһ���ӽڵ�ValueNode
			String str = value.toString();
			if (str.indexOf('[') != -1) {// ����
				// ����ݹ�
				if (value.isArray()) {
					// ������ʱ��,����
					return (ArrayNode) value;
				} else {
					// ����,����������,˵���Ǹ�Ԫ��,�������
					dArrayNode = getArrayNode(value);
				}
			}
			// ������,ȥ��һ����Ԫ��
			continue;
		}
		// ѭ����ϻ���û�з���ֵ
		return dArrayNode;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getPOJOList(String jsonStr, Class<T> objClass) throws IOException {
		JavaType javaType = getCollectionType(ArrayList.class, objClass);
		return (List<T>) mapper.readValue(jsonStr, javaType);

	}

	/**
	 * ��ȡ���͵�Collection Type
	 * 
	 * @param collectionClass ���͵�Collection
	 * @param elementClasses  Ԫ����
	 * @return JavaType Java����
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	// MapתObjectNode
	public static ObjectNode getObjectNode(Map map) throws IOException {
		if (null == map) {
			return JacksonUtil.getObjectNode("");
		}
		return (ObjectNode) mapper.readTree(mapper.writeValueAsString(map));
	}

	// ObjectNodeתPOJO
	public static <T> T getPOJO(ObjectNode objectNode, Class<T> objClass) throws IOException {
		return mapper.readValue(objectNode.toString(), objClass);
	}

	// ObjectNodeתJsonString

	// ObjectNodeתList
	public static List<Map> getList(String jsonStr) throws IOException {
		return mapper.readValue(jsonStr, ArrayList.class);
	}

	// ObjectNodeתMap
	public static Map getMap(String jsonStr) throws IOException {
		Map map = mapper.readValue(jsonStr, HashMap.class);
		CommonUtil.mapTrimToNull(map);
		return map;
	}

	/********************** ����Ϊ���л����ò��� *****************************/

	// mapper2������
	public static ObjectMapper configMapper2() {// ����ע��

		mapper2 = new ObjectMapper().setAnnotationIntrospector(new NonFieldSerializer())// ���е�JsonProperties��������
				.enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN)// ���ÿ�ѧ������
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)// ��ֹjson�в����ڵ��ֶ�,�����л�����
				.disable(Feature.WRITE_NUMBERS_AS_STRINGS);// ��������дΪ�ַ���
					
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
		mapper2.setTimeZone(TimeZone.getTimeZone("GMT+8"));// ���ʱ����8Сʱ����
		return mapper2;

	}

	// mapper1������
	@SuppressWarnings("serial")
	public static ObjectMapper configMapper1() {// ����ע��

		mapper1 = new ObjectMapper().setAnnotationIntrospector(new NonFieldSerializer())// ���е�JsonProperties��������
				.enable(Feature.WRITE_BIGDECIMAL_AS_PLAIN)// ���ÿ�ѧ������
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)// ��ֹjson�в����ڵ��ֶ�,�����л�����
				.disable(Feature.WRITE_NUMBERS_AS_STRINGS);// ��������дΪ�ַ���
		mapper1.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException, JsonProcessingException {
				gen.writeString("");
			}
		});
		// ����ȫ�ֵ�ʱ��ת��
		SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper1.setDateFormat(smt);
		mapper1.setTimeZone(TimeZone.getTimeZone("GMT+8"));// ���ʱ����8Сʱ����
		// �˴�����ע��
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

			// ע���滻JsonKeyֵ
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
	 * ����fastjson
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 **************/
	// ��ȡjson��ʽ�ַ���
	public static String serialize(Object object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);

	}

	/*** xml ������ ***/
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
