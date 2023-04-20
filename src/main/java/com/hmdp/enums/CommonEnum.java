package com.hmdp.enums;


import com.hmdp.exception.BusinessException;

import java.util.Arrays;
import java.util.Optional;

public interface CommonEnum {

	/**
	 * 枚举值
	 */
	Integer getIndex();

	/**
	 * 枚举描述
	 */
	String getDesc();

	/**
	 * 根据code获取枚举值
	 *
	 * @param enumClass 枚举class
	 * @param index     枚举编码
	 * @param <E>       枚举类
	 * @return 枚举值
	 */
	static <E extends Enum<?> & CommonEnum> E getByIndex(Class<E> enumClass, Integer index) {
		E[] values = enumClass.getEnumConstants();
		if (null == values) {
			return null;
		}
		return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getIndex().equals(index)).findFirst().orElse(null);
	}

	static <E extends Enum<?> & CommonEnum> E getByIndexOrThrow(Class<E> enumClass, Integer index, StatusEnum statusEnum) {
		return Optional
				.ofNullable(CommonEnum.getByIndex(enumClass, index))
				.orElseThrow(() -> new BusinessException(statusEnum));
	}

}