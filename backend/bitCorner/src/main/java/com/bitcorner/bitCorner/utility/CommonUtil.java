package com.bitcorner.bitCorner.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CommonUtil {
    public static void copyNonNullProperties(Object source, Object destination, String... ignored) {

        Set<String> ignoredPropertiesSet = new HashSet<>();
        ignoredPropertiesSet.addAll(Arrays.asList(ignored));
        ignoredPropertiesSet.addAll(Arrays.asList(getIgnoreProperties(source)));
        String[] ignoredProperties = new String[ignoredPropertiesSet.size()];
        BeanUtils.copyProperties(source, destination, ignoredPropertiesSet.toArray(ignoredProperties));
    }

    public static String[] getIgnoreProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> nullPropertyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) nullPropertyNames.add(pd.getName());
        }
        String[] result = new String[nullPropertyNames.size()];
        return nullPropertyNames.toArray(result);
    }
}
