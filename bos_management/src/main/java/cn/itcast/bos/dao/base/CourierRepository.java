package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier>{
	
	@Query("update Courier set deltag =1 where id=?")
	@Modifying
	public void updateDelight(Integer id);
	
	@Query("update Courier set deltag =0 where id=?")
	@Modifying
	public void update(Integer id);

	@Query("from Courier where deltag='0'")
	public List<Courier> findName();
	
	
}
