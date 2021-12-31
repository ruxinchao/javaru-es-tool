package com.javaru.es.vo.response;

public class EsQueryResultVo  extends EsResultBase{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private Integer page = 1;// 当前页
    private Long total = 0l;// 总数
    private Integer size = 10;// 每页数量
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
}
