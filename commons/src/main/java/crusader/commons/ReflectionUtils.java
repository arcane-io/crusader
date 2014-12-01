/*
 * Copyright (c) www.arcane.ro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package crusader.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for working with reflection API.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public final class ReflectionUtils {
    public static void handleException(Exception e) {

    }

    public static Object createInstance(String className) 
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, 
            InvocationTargetException, InstantiationException {
        
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    private ReflectionUtils() {
    }
}
