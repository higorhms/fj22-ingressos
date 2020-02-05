# CarsFan

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public String index() {
		return "redirect:swagger-ui.html";
	}
}
