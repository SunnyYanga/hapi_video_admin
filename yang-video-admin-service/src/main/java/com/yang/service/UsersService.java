package com.yang.service;

import com.yang.pojo.Users;
import com.yang.utils.PagedResult;

public interface UsersService {

	PagedResult queryUsers(Users user, Integer page, Integer pageSize);
	
}
