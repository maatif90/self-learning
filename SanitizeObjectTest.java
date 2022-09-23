
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SanitizeObjectTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        String xyz = "a" + "c";
        Test t =new Test("aatif",123,new Emp("aquib"), Arrays.asList("12b"));
        Test copy = sanitizedCopyObject(t);
        System.out.println(copy);




    }


    private static <T> T sanitizedCopyObject(T entity) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = entity.getClass();
        T newEntity = (T) entity.getClass().newInstance();

        while (clazz != null) {
            copyFields(entity, newEntity, clazz);
            clazz = clazz.getSuperclass();
        }

        return newEntity;
    }

    private static <T> T copyFields(T entity, T newEntity, Class<?> clazz) throws IllegalAccessException, InstantiationException {
        for (Field field : clazz.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object memVariable = field.get(entity);
            if(!field.getType().isPrimitive() && memVariable instanceof Collection == false){
                if(field.getType().getSimpleName().equals("String")){
                     memVariable =  memVariable+"testing";//TODO code to sanitize.
                }
                else {
                    memVariable=sanitizedCopyObject(field.get(entity));
                }
            }
            field.set(newEntity, memVariable);
            field.setAccessible(accessible);

        }
        return newEntity;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@ToString
class Test{
    String a;
    int b;
    Emp c;
    List<String> m;
}
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Emp {
    String abc ;
