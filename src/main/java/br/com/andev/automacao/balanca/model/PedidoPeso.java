package br.com.andev.automacao.balanca.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="pedido_peso")
@Entity
public class PedidoPeso {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="num_nf")
	private Long numNotaFiscal;
	
	@Column(name="num_pedido")
	private Long numPedido;
	
	@Column(name="peso")
	private Double peso;
	
	@Column(name="unidade")
	private String unidade;
	
	@Column(name="operador")
	private String operador;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_gravacao")
	private Date dataRegistro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumNotaFiscal() {
		return numNotaFiscal;
	}

	public void setNumNotaFiscal(Long numNotaFiscal) {
		this.numNotaFiscal = numNotaFiscal;
	}

	public Long getNumPedido() {
		return numPedido;
	}

	public void setNumPedido(Long numPedido) {
		this.numPedido = numPedido;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

}
