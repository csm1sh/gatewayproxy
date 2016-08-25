package com.hagongda.lightmodbus.util;


    import java.io.File;
    import java.io.IOException;
    import java.lang.reflect.Type;
    import java.util.HashMap;
    import java.util.Map;

    import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
    import com.fasterxml.jackson.annotation.PropertyAccessor;
    import com.fasterxml.jackson.core.JsonGenerationException;
    import com.fasterxml.jackson.databind.JsonMappingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.SerializationFeature;

    public class Json2Object<T>
    {
        public static Map toMap(File json){
            Map obj = null; 
             try
            {
               obj = new ObjectMapper().readValue(json, HashMap.class);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             return obj;
        }
        
        public  T toMap(String json){
            T obj = null; 
             try
            {
                 Type[] parameterizedTypes = ReflectionUtil.getParameterizedTypes(this);
                 @SuppressWarnings("unchecked")
                Class<T> clazz = (Class<T>)ReflectionUtil.getClass(parameterizedTypes[0]);
                 obj = (T)new ObjectMapper().readValue(json, clazz);
                 return obj;
               
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             return obj;
        }

        
        
         public static String  object2Json( Object obj)
        {
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                mapper.setVisibility(PropertyAccessor.SETTER,
                        Visibility.PUBLIC_ONLY);
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                return mapper.writeValueAsString( obj);
            }
            catch (JsonGenerationException e)
            {
                e.printStackTrace();
            }
            catch (JsonMappingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            return null;
        }
      
    }

