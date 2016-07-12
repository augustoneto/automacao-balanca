package br.com.andev.automacao.balanca.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-11T23:02:37.077-0300")
@StaticMetamodel(PedidoPeso.class)
public class PedidoPeso_ {
	public static volatile SingularAttribute<PedidoPeso, Long> id;
	public static volatile SingularAttribute<PedidoPeso, Long> numNotaFiscal;
	public static volatile SingularAttribute<PedidoPeso, Long> numPedido;
	public static volatile SingularAttribute<PedidoPeso, Double> peso;
	public static volatile SingularAttribute<PedidoPeso, String> unidade;
	public static volatile SingularAttribute<PedidoPeso, String> operador;
	public static volatile SingularAttribute<PedidoPeso, Date> dataRegistro;
}
