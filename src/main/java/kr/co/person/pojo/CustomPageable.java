package kr.co.person.pojo;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class CustomPageable implements Pageable, Serializable {
	
	private static final long serialVersionUID = -2545804271584732766L;
	
	private int page;
	private int size;
	private Sort sort;
	
	public CustomPageable(int page, int size, Sort sort) {
		this.page = page;
		this.size = size;
		this.sort = sort;
	}

	public CustomPageable(int page, int size, Direction direction, String... properties) {
		this(page, size, new Sort(direction, properties));
	}
	

	public CustomPageable previous() {
		return getPageNumber() == 0
				?this
				:new CustomPageable(getPageNumber() - 1, getPageSize(), getSort());
	}
	
	@Override
	public boolean hasPrevious() {
		return page > 0;
	}
	
	@Override
	public Pageable next() {
		return new CustomPageable(getPageNumber() + 1, getPageSize(), getSort());
	}
	
	@Override
	public Pageable first() {
		return new CustomPageable(0, getPageSize(), getSort());
	}

	@Override
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}
	
	@Override
	public int getPageSize() {
		return size;
	}

	@Override
	public int getPageNumber() {
		return page;
	}
	
	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public int getOffset() {
		return page * size;
	}
}
