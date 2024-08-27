package xyz.pary.webp.jna;

import com.sun.jna.DefaultTypeMapper;

public class EnumTypeMapper extends DefaultTypeMapper {

    public EnumTypeMapper() {
        addTypeConverter(JnaEnum.class, new EnumConverter());
    }
}
