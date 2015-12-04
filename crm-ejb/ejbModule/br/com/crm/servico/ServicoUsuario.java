/**
 * 
 */
package br.com.crm.servico;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.crm.entidades.pessoas.Usuario;
import br.com.crm.entidades.pessoas.autorizacao.Permissao;
import br.com.crm.modelo.dao.UsuarioDAO;
import br.com.crm.modelo.excecoes.ExcecaoModelo;
import br.com.crm.servico.dto.PermissaoDTO;
import br.com.crm.servico.dto.TipoPermissaoDTO;
import br.com.crm.servico.dto.UsuarioDTO;
import br.com.crm.servico.excecao.ExcecaoServico;

/**
 * Classe que define os métodos padrão de um serviço
 */
@Stateless
public class ServicoUsuario implements IServico<UsuarioDTO>{

	/**
	 * Injetando usuario DAO para manipulação dos dados do usuário
	 */
	@Inject
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	/**
	 * {@link IServico#incluir(Object)}
	 */
	public void incluir(UsuarioDTO usuarioDTO) throws ExcecaoServico {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioDTO.getNome());
		usuario.setCpf(usuarioDTO.getCpf());
		usuario.setEndereco(usuarioDTO.getEndereco());		
		usuario.setLogin(usuarioDTO.getLogin());
		usuario.setAtivo(usuarioDTO.getAtivo());
		usuario.setSenha(usuarioDTO.getSenha());
		usuario.setConfirmaSenha(usuarioDTO.getConfirmaSenha());
		
		try {
			usuarioDAO.incluir(usuario);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível salvar os dados do usuário");
		}
	}

	/**
	 * {@link IServico#alterar(Object)}
	 */
	public void alterar(UsuarioDTO usuarioDTO) throws ExcecaoServico {
		Usuario usuario = new Usuario();
		usuario.setId(usuarioDTO.getId());
		
		try {
			usuario = usuarioDAO.recuperar(usuario);
			usuario.setAtivo(usuarioDTO.getAtivo());
			usuario.setCpf(usuarioDTO.getCpf());
			usuario.setEndereco(usuarioDTO.getEndereco());
			usuario.setLogin(usuarioDTO.getLogin());
			usuario.setNome(usuarioDTO.getNome());
			if(usuarioDTO.getSenha() != null){
				usuario.setSenha(usuarioDTO.getSenha());
			}
			
			usuarioDAO.alterar(usuario);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível salvar os dados do usuário");
		}
		
	}

	/**
	 * {@link IServico#excluir(Object)}
	 */
	public void excluir(UsuarioDTO usuarioDTO) throws ExcecaoServico {
		try {
			Usuario usuario = new Usuario();
			usuario.setId(usuarioDTO.getId());			
			usuarioDAO.excluir(usuario);
		} catch (ExcecaoModelo e) {
			new ExcecaoServico("Não foi possível excluir os dados do usuário");
		}
		
	}

	/**
	 * {@link IServico#listar()}
	 */
	public List<UsuarioDTO> listar() throws ExcecaoServico {		
		try {
			List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();
			List<Usuario> usuarios = usuarioDAO.listar();
			
			for(Usuario usuario : usuarios){				
				usuariosDTO.add(transformarUsuarioDTO(usuario));
			}

			return usuariosDTO;
			
		} catch (ExcecaoModelo e) {
			return null;
		}		
	}

	/**
	 * {@link IServico#recuperar(Object)}
	 */
	public UsuarioDTO recuperar(UsuarioDTO usuarioDTO) throws ExcecaoServico {
		try {
			Usuario usuario = new Usuario();
			usuario.setId(usuarioDTO.getId());			
			return transformarUsuarioDTO(usuarioDAO.recuperar(usuario));
		} catch (ExcecaoModelo e) {
			return null;
		}
	}	
	
	/**
	 * Método que transforma uma entidade usuário em usuarioDTO
	 */
	private UsuarioDTO transformarUsuarioDTO(Usuario usuario){
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(usuario.getId());
		usuarioDTO.setNome(usuario.getNome());
		usuarioDTO.setCpf(usuario.getCpf());
		usuarioDTO.setEndereco(usuario.getEndereco());		
		usuarioDTO.setLogin(usuario.getLogin());
		usuarioDTO.setAtivo(usuario.getAtivo());
		
		if(usuario.getListaPermissoes().size() > 0){
			
			List<PermissaoDTO> listaPermissoesDTO = new ArrayList<PermissaoDTO>();
			
			for(Permissao permissao : usuario.getListaPermissoes()){
				PermissaoDTO permissaoDTO = new PermissaoDTO();
				permissaoDTO.setId(permissao.getId());
				permissaoDTO.setTela(permissao.getTela());
				
				if(permissao.getTipoPermissao() != null){
					TipoPermissaoDTO tipoPermissaoDTO = new TipoPermissaoDTO();
					tipoPermissaoDTO.setId(permissao.getTipoPermissao().getId());
					tipoPermissaoDTO.setSigla(permissao.getTipoPermissao().getSigla());
					tipoPermissaoDTO.setDescricao(permissao.getTipoPermissao().getDescricao());
					permissaoDTO.setTipoPermissao(tipoPermissaoDTO);
				}

				listaPermissoesDTO.add(permissaoDTO);
			}
				
			usuarioDTO.setListaPermissoes(listaPermissoesDTO);					
		}
		
		return usuarioDTO;
	}
}