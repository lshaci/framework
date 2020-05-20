package top.lshaci.framework.swagger;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.ToString;
import top.lshaci.framework.utils.FreemarkerUtil;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Swagger2Doc
 *
 * @author lshaci
 * @since 1.0.8
 */
public class Swagger2Doc {

    /**
     * 将swagger的json文档生成doc, 并写入到输出流中
     *
     * @param json json格式的swagger文档
     * @param os 需要写入的输入流
     * @throws Exception 失败抛出异常
     */
    public static void generate(String json, OutputStream os) throws Exception {
        JSON.DEFAULT_PARSER_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        JSONObject jsonObject = JSON.parseObject(json);
        List<Model> models = definitions(jsonObject.getJSONObject("definitions"));

        List<Path> paths = paths(jsonObject.getJSONObject("paths"));

        Map<String, Object> model = new HashMap<>();
        model.put("models", models);
        model.put("paths", paths);

        FreemarkerUtil.generate("swagger.ftl", model, os);
    }

    /**
     * 解析接口
     *
     * @param paths 接口信息json对象
     * @return 接口信息
     */
    private static List<Path> paths(JSONObject paths) {
        return paths.keySet().stream().flatMap(url -> {
            JSONObject value = paths.getJSONObject(url);
            return value.keySet().stream().map(method -> {
                JSONObject req = value.getJSONObject(method);
                Path path = new Path();
                path.setUrl(url);
                path.setMethod(method);
                path.setName(req.getString("summary"));
                JSONArray consumes = req.getJSONArray("consumes");
                if (CollectionUtil.isNotEmpty(consumes)) {
                    path.setConsumes(consumes.stream().map(Object::toString).collect(Collectors.joining(", ")));
                }
                List<Parameter> parameters = parameters(req.getJSONArray("parameters"));
                Field response = responses(req.getJSONObject("responses"));
                path.setParameters(parameters);
                path.setResponse(response);
                return path;
            });
        }).collect(Collectors.toList());
    }

    /**
     * 解析成功响应的数据格式
     *
     * @param responses 所有的响应信息
     * @return 成功响应的数据格式
     */
    private static Field responses(JSONObject responses) {
        Field field = new Field();
        JSONObject success = responses.getJSONObject("200");
        handleResponse(success, field);
        return field;
    }

    /**
     * 处理成功响应的数据
     *
     * @param success 成功响应的json信息
     * @param field 成功响应数据
     */
    private static void handleResponse(JSONObject success, Field field) {
        handleSchema(success, field);
        String format = field.getFormat();
        if (StrUtil.isBlank(format)) {
            field.setType("void");
            return;
        }
        String[] formats = format.replaceAll("«|»", "_").split("_");
        String description = Arrays.stream(formats).reduce((f, s) -> s).get();
        field.setDescription(description);
    }

    /**
     * 解析所有的请求参数
     *
     * @param parameters 请求参数json数据
     * @return 请求参数集合
     */
    private static List<Parameter> parameters(JSONArray parameters) {
        if (CollectionUtil.isEmpty(parameters)) {
            return null;
        }

        return parameters.stream().map(p -> {
            JSONObject jsonObject = (JSONObject) p;
            Parameter parameter = new Parameter();
            parameter.setIn(jsonObject.getString("in"));
            parameter.setName(jsonObject.getString("name"));
            parameter.setType(jsonObject.getString("type"));
            parameter.setRequire(jsonObject.getBoolean("required"));
            parameter.setDescription(jsonObject.getString("description"));

            handleSchema(jsonObject, parameter);
            return parameter;
        }).collect(Collectors.toList());
    }

    /**
     * 处理属性中的schema
     *
     * @param jsonObject json对象
     * @param field 字段信息
     */
    private static void handleSchema(JSONObject jsonObject, Field field) {
        JSONObject schema = jsonObject.getJSONObject("schema");
        if (Objects.isNull(schema)) {
            return;
        }

        handleItems(schema, field);
    }

    /**
     * 解析所有的数据模型
     *
     * @param definitions 数据模型json数据
     * @return 数据模型对象
     */
    public static List<Model> definitions(JSONObject definitions) {
        return definitions.keySet().stream().map(k -> {
            JSONObject value = definitions.getJSONObject(k);
            JSONObject properties = value.getJSONObject("properties");
            JSONArray required = value.getJSONArray("required");
            List<String> requiredField = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(required)) {
                requiredField = value.getJSONArray("required").stream().map(Object::toString).collect(Collectors.toList());
            }

            List<Field> fields = properties2Field(properties, requiredField);
            Model model = new Model();
            model.setTitle(k);
            model.setType(value.getString("type"));
            model.setFields(fields);
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 将属性转换为字段对象
     *
     * @param properties 数据模型中的属性
     * @param required 必须的字段
     * @return 字段对象集合
     */
    public static List<Field> properties2Field(JSONObject properties, List<String> required) {
        return properties.keySet().stream().map(k -> {
            JSONObject value = properties.getJSONObject(k);
            Field field = new Field();
            field.setName(k);
            field.setType(value.getString("type"));
            field.setFormat(value.getString("format"));
            field.setRequire(required.contains(k));
            field.setDescription(value.getString("description"));
            handleItems(value, field);

            return field;
        }).collect(Collectors.toList());
    }

    /**
     * 处理字段json数据中的items
     *
     * @param value 字段json数据
     * @param field 字段信息
     */
    private static void handleItems(JSONObject value, Field field) {
        JSONObject items = value.getJSONObject("items");
        String ref = value.getString("$ref");
        if (Objects.isNull(items) && StrUtil.isBlank(ref)) {
            return;
        }

        if (StrUtil.isBlank(ref)) {
            ref = items.getString("$ref");
        }
        String format = "";
        if (StrUtil.isBlank(ref)) {
            String type = items.getString("type");
            format = StrUtil.isNotBlank(type) ? type : "";
        } else {
            field.setRef(true);
            format = Arrays.stream(ref.split("/")).reduce((f, s) -> s).get();
        }

        field.setFormat(format);
    }

    /**
     * 接口信息
     *
     * @author lshaci
     * @since 1.0.8
     */
    @Data
    public static class Path {
        /**
         * 接口名称
         */
        String name;
        /**
         * 请求地址
         */
        String url;
        /**
         * 请求方法
         */
        String method;
        /**
         * 数据类型
         */
        String consumes;

        /**
         * 成功响应信息
         */
        Field response;
        /**
         * 请求参数集合
         */
        List<Parameter> parameters;
    }

    /**
     * 请求参数
     *
     * @author lshaci
     * @since 1.0.8
     */
    @Data
    @ToString(callSuper = true)
    public static class Parameter extends Field {
        /**
         * 参数位置
         */
        String in;
    }

    /**
     * 数据模型
     *
     * @author lshaci
     * @since 1.0.8
     */
    @Data
    public static class Model {
        /**
         * 数据名称
         */
        String title;
        /**
         * 数据类型
         */
        String type;
        /**
         * 模型中的字段集合
         */
        List<Field> fields;
    }

    /**
     * 字段信息
     *
     * @author lshaci
     * @since 1.0.8
     */
    @Data
    public static class Field {
        /**
         * 名称
         */
        String name;
        /**
         * 类型
         */
        String type;
        /**
         * 格式化信息
         */
        String format;
        /**
         * 是否必须
         */
        boolean require;
        /**
         * 是否为引用类型
         */
        Boolean ref;
        /**
         * 描述
         */
        String description;

        /**
         * 获取字符串的是否必须
         *
         * @return 是否必须的字符串
         */
        public String getRequire() {
            return require ? "true" : "false";
        }

        public Boolean getRef() {
            return Objects.equals(ref, Boolean.TRUE);
        }

        public String getType() {
            return StrUtil.isNotBlank(type) ? type : "object";
        }
    }
}
