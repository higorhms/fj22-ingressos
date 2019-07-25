package br.com.caelum.ingresso.model.form;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;

public class SessaoForm {
	
	@NotNull
	private Integer salaId;
	
	@DateTimeFormat(pattern="HH:mm")
	@NotNull
	private LocalTime horario;
	
	@NotNull
	private Integer filmeId;
	
	// Metodo para validacao dos dados da view que cadastra sessao
	public Sessao toSessao(SalaDao salaDao, FilmeDao filmeDao) {
		//coletando dados de acordo com os ids especificos
		Filme filme = filmeDao.findOne(filmeId);
		Sala sala = salaDao.findOne(salaId);
		
		//Populando a sessao com os dados validados para futuramente persistir no BD
		Sessao sessao = new Sessao(this.horario, filme, sala);
		
		return sessao;
	}

	public Integer getSalaId() {
		return salaId;
	}

	public void setSalaId(Integer salaId) {
		this.salaId = salaId;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public Integer getFilmeId() {
		return filmeId;
	}

	public void setFilmeId(Integer filmeId) {
		this.filmeId = filmeId;
	}
	
	
}
