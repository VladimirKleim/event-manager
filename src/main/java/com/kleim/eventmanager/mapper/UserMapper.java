package com.kleim.eventmanager.mapper;

import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.pojo.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.WARN
)
public interface UserMapper {

     UserDTO toDtoUser(User user);

}
