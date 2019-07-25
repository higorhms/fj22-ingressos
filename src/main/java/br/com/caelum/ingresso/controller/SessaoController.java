package br.com.caelum.ingresso.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.ImagemCapa;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.form.SessaoForm;
import br.com.caelum.ingresso.rest.OmdbClient;
import br.com.caelum.ingresso.validacao.GerenciadorDeSessao;

@Controller
public class SessaoController {

	@Autowired
	private SessaoDao sessaoDao;

	@Autowired
	private SalaDao salaDao;

	@Autowired
	private FilmeDao filmeDao;
	
	@Autowired
	private OmdbClient client;

	// O paremetro form se refere aos dados já preenchidos no caso de erro de
	// validação
	@GetMapping("/admin/sessao")
	public ModelAndView form(@RequestParam("salaId") Integer salaId, SessaoForm form) {

		ModelAndView modelAndView = new ModelAndView("sessao/sessao");

		modelAndView.addObject("sala", salaDao.findOne(salaId));
		modelAndView.addObject("filmes", filmeDao.findAll());
		modelAndView.addObject("form", form);

		return modelAndView;

	}

	@PostMapping("/admin/sessao")
	@Transactional // para que so ocorra a transacao se nao der erro, somente grava os dados se
					// tudo estiver certo
	public ModelAndView salva(@Valid SessaoForm form, BindingResult result) {

		// o parametro form é passado para o metodo form acima para que os dados validos
		// digitados pelo usuario permaneçam na view
		if (result.hasErrors())
			return form(form.getSalaId(), form);

		// validando dados atraves do metodo ToSessao da classe SessaoForm
		Sessao sessao = form.toSessao(salaDao, filmeDao);

		// Criando uma lista com as sessoes da sala
		List<Sessao> sessoesDaSala = sessaoDao.buscaSessoesDaSala(sessao.getSala());

		// instanciando o gerenciador/lógica de validaçáo da sessao
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);

		// Gerenciando as sessoes verificando se elas acabam no mesmo dia e se nao estáo
		// se encavalando
		if (gerenciador.cabe(sessao)) {
			sessaoDao.save(sessao);
			return new ModelAndView("redirect:/admin/sala/" + form.getSalaId() + "/sessoes");
		}
		// Caso náo seja validada a sesao, retorna a view passando como parametro o form
		// preenchido com dados que estáo corretos
		return form(form.getSalaId(), form);
	}

	@GetMapping("/sessao/{id}/lugares")
	public ModelAndView lugaresNaSessao(@PathVariable("id") Integer sessaoId) {
		ModelAndView modelAndView = new ModelAndView("sessao/lugares");
		Sessao sessao = sessaoDao.findOne(sessaoId);
		
		Optional<ImagemCapa> imagemCapa = client.request(sessao.getFilme(), ImagemCapa.class);

		modelAndView.addObject("sessao", sessao);
		modelAndView.addObject("imagemCapa", imagemCapa.orElse(new ImagemCapa()));
		return modelAndView;
	}

}
