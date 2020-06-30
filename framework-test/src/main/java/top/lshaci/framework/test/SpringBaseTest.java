package top.lshaci.framework.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Base Spring Test
 *
 * @author lshaci
 * @since 0.0.1
 *
 * @param <B> The test bean type
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class SpringBaseTest<B> {

    @Autowired
    protected B bean;
}
