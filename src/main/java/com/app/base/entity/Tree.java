package com.app.base.entity;


public interface Tree<T> {

	/**
	 * 获取上级节点PK
	 * @return
	 */
	public T getParent();

	/**
	 * 获取层级
	 * @return
	 */
	public Integer getLevel();

	/**
	 * 获取当前层级节点序号
	 * @return
	 */
	public Tree<T> getAttributes();

	public String getText();

	public String getIconCls();

//	public List<Tree<T>> getChildren();
}
