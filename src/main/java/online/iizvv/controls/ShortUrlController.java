package online.iizvv.controls;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-05 16:30
 * @description：TODO
 * @version: 1.0
 */
@RestController
@RequestMapping(value="/app")
public class ShortUrlController {

    @RequestMapping(value="/{hex}", method=RequestMethod.GET)
    public void getUser(@PathVariable String hex) {

    }

}
