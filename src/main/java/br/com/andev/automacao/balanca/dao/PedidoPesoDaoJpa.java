package br.com.andev.automacao.balanca.dao;

import javax.persistence.EntityManager;

import br.com.andev.automacao.balanca.model.PedidoPeso;

public class PedidoPesoDaoJpa implements PedidoPesoDao {
	
	private EntityManager em;
	
	public PedidoPesoDaoJpa(EntityManager em) {
		this.em = em;
	}
	
	public void salvar(PedidoPeso pedidoPeso) {
		em.persist(pedidoPeso);
	}

}
