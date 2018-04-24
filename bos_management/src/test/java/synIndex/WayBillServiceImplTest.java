package synIndex;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.service.base.WayBillService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class WayBillServiceImplTest {
	
	@Autowired
	private WayBillService wayBillService;
	
	@Test
	public void testSynIndex() {
		wayBillService.synIndex();
	}

}
