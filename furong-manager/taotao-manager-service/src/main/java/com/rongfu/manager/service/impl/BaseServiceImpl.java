package com.rongfu.manager.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.rongfu.manager.pojo.BasePojo;
import com.rongfu.manager.service.BaseService;

public class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {

	@Autowired
	private Mapper<T> mapper;

	private Class classz;

	public BaseServiceImpl() {
		// 获取父类的type
		Type type = this.getClass().getGenericSuperclass();

		// 强转为ParameterizedType，可以使用获取泛型类型的方法
		ParameterizedType pType = (ParameterizedType) type;

		// 获取泛型的class
		this.classz = (Class<T>) pType.getActualTypeArguments()[0];
	}

	@Override
	public T queryById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<T> queryAll() {
		// TODO Auto-generated method stub
		return mapper.select(null);
	}

	@Override
	public Integer queryCountByWhere(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public List<T> queryListByWhere(T t) {
		return mapper.select(t);
	}

	@Override
	public List<T> queryByPage(Integer page, Integer rows, T t) {
		PageHelper.startPage(page, rows);
		List<T> list = mapper.select(t);
		return list;
	}

	@Override
	public T queryOne(T t) {
		return mapper.selectOne(t);
	}

	@Override
	public void save(T t) {
		// 设置默认的新增与修改时间
		if (t.getCreated() == null) {
			t.setCreated(new Date());
			t.setUpdated(t.getCreated());
		}
		mapper.insert(t);
	}

	@Override
	public void saveSelective(T t) {
		// 设置默认的新增与修改时间
		if (t.getCreated() == null) {
			t.setCreated(new Date());
			t.setUpdated(t.getCreated());
		}
		mapper.insertSelective(t);

	}

	@Override
	public void updateById(T t) {
		// 设置默认的新增与修改时间
		if (t.getUpdated() == null) {
			t.setUpdated(new Date());
		}
		mapper.updateByPrimaryKey(t);
	}

	@Override
	public void updateByIdSelective(T t) {
		// 设置默认的新增与修改时间
		if (t.getUpdated() == null) {
			t.setUpdated(new Date());
		}
		mapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public void deleteById(Long id) {
		mapper.deleteByPrimaryKey(id);

	}

	@Override
	public void deleteByIds(List<Object> ids) {
		// 设置删除条件
		Example example = new Example(this.classz);
		Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
		// 执行删除
		mapper.deleteByExample(example);
	}

}
