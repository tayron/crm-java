/**
 * 
 */
package br.com.crm.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.crm.daos.GrupoDAO;
import br.com.crm.dtos.GrupoDTO;
import br.com.crm.entidades.Grupo;
import br.com.crm.excecoes.ExcecaoModelo;
import br.com.crm.excecoes.ExcecaoServico;
import br.com.crm.repositorios.IDaoRepositorio;

/**
 * Classe que disponibiliza serviços para manipulação do grupo
 */
@Stateless
public class ServicoGrupo implements IServicoGrupo{

	/**
	 * Injetando dao repositório para manipulação dos dados do grupo
	 */
	@EJB
	IDaoRepositorio daoRepotorio;
	
	/**
	 * Armazena o GrupoDAO para manipulacao dos dados do grupo
	 */
	GrupoDAO grupoDAO;
	
	/**
	 * Busca o dao grupo no repositório e armazena em grupoDAO
	 */
	@PostConstruct
	public void initialize(){
		grupoDAO = daoRepotorio.getGrupo();
	}
	
	/**
	 * @see IServicoGrupo#incluir(GrupoDTO)
	 */
	@Override
	public void incluir(GrupoDTO grupoDTO) throws ExcecaoServico {
		Grupo grupo = new Grupo();
		grupo.setNome(grupoDTO.getNome());
		
		try {
			grupoDAO.incluir(grupo);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível salvar os dados do usuário");
		}
	}

	/**
	 * @see IServicoGrupo#alterar(GrupoDTO)
	 */
	@Override
	public void alterar(GrupoDTO grupoDTO) throws ExcecaoServico {
		Grupo grupo = new Grupo();
		grupo.setId(grupoDTO.getId());
		
		try {
			grupo = grupoDAO.recuperar(grupo);
			grupo.setNome(grupoDTO.getNome());
			grupoDAO.alterar(grupo);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível salvar os dados do grupo");
		}
		
	}

	/**
	 * @see IServicoGrupo#excluir(GrupoDTO)
	 */
	@Override
	public void excluir(GrupoDTO grupoDTO) throws ExcecaoServico {
		try {
			Grupo grupo = new Grupo();
			grupo.setId(grupoDTO.getId());	
			grupo = grupoDAO.recuperar(grupo);
			
			grupoDAO.excluir(grupo);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível excluir os dados do grupo");
		}
		
	}

	/**
	 * @see servico.IServicoGrupo#listar()
	 */
	@Override
	public List<GrupoDTO> listar() throws ExcecaoServico {		
		try {
			List<GrupoDTO> gruposDTO = new ArrayList<GrupoDTO>();
			List<Grupo> grupos = grupoDAO.listar();
			
			for(Grupo grupo : grupos){	
				GrupoDTO grupoDTO = new GrupoDTO();
				grupoDTO.setId(grupo.getId());
				grupoDTO.setNome(grupo.getNome());
				gruposDTO.add(grupoDTO);
			}

			return gruposDTO;
			
		} catch (ExcecaoModelo e) {
			return null;
		}		
	}

	/**
	 * @see IServicoGrupo#recuperar(GrupoDTO)
	 */
	@Override
	public GrupoDTO recuperar(GrupoDTO grupoDTO) throws ExcecaoServico {
		try {
			Grupo grupo = new Grupo();
			grupo.setId(grupoDTO.getId());
			grupo = grupoDAO.recuperar(grupo);
			
			grupoDTO.setId(grupo.getId());
			grupoDTO.setNome(grupo.getNome());
			return grupoDTO;
		} catch (ExcecaoModelo e) {
			return null;
		}
	}
}
