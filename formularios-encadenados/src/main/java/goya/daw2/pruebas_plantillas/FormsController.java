package goya.daw2.pruebas_plantillas;

import java.util.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FormsController {

	static final String[] SIGNOS = { "", "Aries", "Tauro", "Géminis", "Cáncer", "Leo", "Virgo", "Libra", "Escorpio",
			"Sagitario", "Capricornio", "Acuario", "Piscis" };
	static final String[] AFICCIONES = { "Deportes", "Juerga", "Lectura", "Relaciones sociales" };

	@GetMapping("/cookies")
	public String cookiesInicio(Model m) {
		m.addAttribute("numEtapa", 1);
		m.addAttribute("signos", SIGNOS);
		m.addAttribute("aficciones", AFICCIONES);
		return "etapa1";
	}

	@PostMapping("/cookies")
	public String cookies(@RequestParam("numEtapa") int numEtapa,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "signo", required = false) String signo,
			@RequestParam(value = "aficciones", required = false) String[] aficciones, Model m, HttpServletRequest req,
			HttpServletResponse resp) {
		m.addAttribute("signos", SIGNOS);
		m.addAttribute("aficciones", AFICCIONES);
		String error = "";

		if (numEtapa == 1) {
			if (nombre == null || nombre.trim().length() < 3)
				error = "Nombre inválido";
			else
				resp.addCookie(new Cookie("nombre", nombre.trim()));
		} else if (numEtapa == 2) {
			if (signo == null || signo.equals("0"))
				error = "Selecciona signo";
			else
				resp.addCookie(new Cookie("signo", signo));
		} else if (numEtapa == 3) {
			if (aficciones == null || aficciones.length == 0)
				error = "Elige aficciones";
			else
				resp.addCookie(new Cookie("afs", String.join(",", aficciones)));
		}

		if (!error.isEmpty()) {
			m.addAttribute("errores", error);
			m.addAttribute("numEtapa", numEtapa);
			return "etapa" + numEtapa;
		}

		numEtapa++;
		m.addAttribute("numEtapa", numEtapa);

		if (numEtapa == 4) {
			String n = "", s = "", a = "";
			if (req.getCookies() != null) {
				for (Cookie c : req.getCookies()) {
					switch (c.getName()) {
					case "nombre" -> n = c.getValue();
					case "signo" -> s = c.getValue();
					case "afs" -> a = c.getValue();
					}
				}
			}
			int pos = Integer.parseInt(s);
			List<String> r = List.of(n, SIGNOS[pos], a);
			m.addAttribute("respuestas", r);
		}

		return "etapa" + numEtapa;
	}

	@GetMapping("/session")
	public String sessionInicio(Model m) {
		m.addAttribute("numEtapa", 1);
		m.addAttribute("signos", SIGNOS);
		m.addAttribute("aficciones", AFICCIONES);
		return "etapa1";
	}

	@PostMapping("/session")
	public String session(@RequestParam("numEtapa") int numEtapa,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "signo", required = false) String signo,
			@RequestParam(value = "aficciones", required = false) String[] aficciones, Model m, HttpSession sesion) {
		m.addAttribute("signos", SIGNOS);
		m.addAttribute("aficciones", AFICCIONES);
		String error = "";

		if (numEtapa == 1) {
			if (nombre == null || nombre.trim().length() < 3)
				error = "Nombre inválido";
			else
				sesion.setAttribute("nombre", nombre.trim());
		} else if (numEtapa == 2) {
			if (signo == null || signo.equals("0"))
				error = "Selecciona signo";
			else
				sesion.setAttribute("signo", signo);
		} else if (numEtapa == 3) {
			if (aficciones == null || aficciones.length == 0)
				error = "Elige aficciones";
			else
				sesion.setAttribute("afs", Arrays.asList(aficciones));
		}

		if (!error.isEmpty()) {
			m.addAttribute("errores", error);
			m.addAttribute("numEtapa", numEtapa);
			return "etapa" + numEtapa;
		}

		numEtapa++;
		m.addAttribute("numEtapa", numEtapa);

		if (numEtapa == 4) {
			String n = (String) sesion.getAttribute("nombre");
			int pos = Integer.parseInt((String) sesion.getAttribute("signo"));
			List<String> a = (List<String>) sesion.getAttribute("afs");
			m.addAttribute("respuestas", List.of(n, SIGNOS[pos], String.join(", ", a)));
		}

		return "etapa" + numEtapa;
	}
}
