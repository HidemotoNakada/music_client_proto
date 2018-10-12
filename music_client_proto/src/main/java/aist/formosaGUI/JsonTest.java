package aist.formosaGUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

public class JsonTest extends App {
	public static void main(String [] args)  {

		try {
			Map<String, Object> car = new HashMap<String, Object>();
			car.put("brand", "BMW");
			car.put("door", 4);

			List cars = new ArrayList();
			cars.add(car);

			Map fleet = new HashMap();
			fleet.put("cars", cars);

			ObjectMapper mapper = JsonFactory.create();
			String json = mapper.writeValueAsString(car);
			System.out.println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
