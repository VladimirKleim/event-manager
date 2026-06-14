package com.kleim.eventmanager.mapper;

import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.pojo.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDbMapper {

    User toDomain(UserEntity user);
    UserEntity toEntityUser(User user);

}
