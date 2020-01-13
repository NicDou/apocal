package com.jd.common.vo;

import lombok.Data;

/**
 * @author DOU
 * @date 2019-09-10 16:25 自动绑定用户信息
 */
@Data
public class UserVO {

	private long id;

	private String username;

	private int userType;

	private long officeId;

	private String officeName;

	private boolean canRead;

	private boolean canWrite;

}
