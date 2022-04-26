package ru.gb.web.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gb.api.security.dto.UserDto;
import ru.gb.entity.security.AccountUser;

@Mapper
public interface UserMapper {
    @Mapping(source = "userId", target = "id")
    AccountUser toAccountUser(UserDto userDto);

    @Mapping(source = "id", target = "userId")
    UserDto toUserDto(AccountUser accountUser);
}