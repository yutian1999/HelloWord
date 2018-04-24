package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaRepository extends JpaRepository<FixedArea,String>,JpaSpecificationExecutor<FixedArea>{
	
	@Query("delete from FixedArea where id=?")
	@Modifying
	public void del(String id);
}
