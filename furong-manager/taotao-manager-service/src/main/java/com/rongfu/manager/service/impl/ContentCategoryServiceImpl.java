package com.rongfu.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongfu.manager.mapper.ContentCategoryMapper;
import com.rongfu.manager.pojo.ContentCategory;
import com.rongfu.manager.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {

	@Override
	public List<ContentCategory> queryContentCategoryByParentId(Long parentId) {
		ContentCategory t = new ContentCategory();
		t.setParentId(parentId);
		List<ContentCategory> list = super.queryListByWhere(t);
		return list;
	}

	@Override
	public ContentCategory saveContentCategory(ContentCategory category) {
		category.setStatus(1);
		category.setIsParent(false);
		// 保存分类
		super.saveSelective(category);

		// 父节点查询，如果父节点为叶子节点，更新为父节点
		ContentCategory parent = super.queryById(category.getParentId());
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			super.updateByIdSelective(parent);
		}
		// 设置父节点为true
		return category;
	}

	@Override
	public void deleteContentCategory(ContentCategory category) {
		// 将要删除的id列表
		List<Object> ids = new ArrayList<>();
		// 把本身先添加进来
		ids.add(category.getId());
		//查询当前节点下所有子节点
		this.queryCategoryByParentId(category.getId(), ids);
		// 删除分类
		super.deleteByIds(ids);
		
		
		//如果当前节点的父节点没有子节点，把父节点改为叶子节点
		ContentCategory t = new ContentCategory();
		t.setParentId(category.getParentId());
		//当前父节点下有子节点的总记录数
		Integer count = super.queryCountByWhere(t);
		if(count == 0){
			t = new ContentCategory();
			t.setId(category.getParentId());
			t.setIsParent(false);
			//更新父节点
			super.updateByIdSelective(t);
		}
	}

	/**
	 * 递归查询父节点下的所有子节点
	 * 
	 * @param parentId
	 * @param ids
	 */
	private void queryCategoryByParentId(Long parentId, List<Object> ids) {
		ContentCategory t = new ContentCategory();
		t.setParentId(parentId);
		// 跟据父id搜索数据列表
		List<ContentCategory> list = super.queryListByWhere(t);
		for (ContentCategory ct : list) {
			ids.add(ct.getId());
			// 如果子节点下还有子节点，调用本身
			if (ct.getIsParent()) {
				this.queryCategoryByParentId(ct.getId(), ids);
			}
		}
	}
}
