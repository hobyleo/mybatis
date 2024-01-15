package com.hoby.mapper;

import com.hoby.entity.User;

/**
 * @author hoby
 * @since 2024-01-15
 */
public interface UserMapper {

    User selectById(Integer id);

}
