package Projecte2Jav;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="treballador")
@XmlType(propOrder = {"id_treballador","dni","nom","cognom","telefon","correu","num_identificatiu","minuts","nomina","any","mes","num_nomina","id_nomina","jornades"})
public class Treballador {
 private int id_treballador;
 private String dni;
 private String nom;
 private String cognom;
 private int telefon;
 private String correu;
 private String num_identificatiu;
 private int minuts;
 private int nomina;
 private int any;
 private int mes;
 private String num_nomina;
 private int id_nomina;
 private int jornades;
 
public Treballador() {
	
}

@XmlAttribute(name="id_treballador")
public int getId_treballador() {
	return id_treballador;
}

public void setId_treballador(int id_treballador) {
	this.id_treballador = id_treballador;
}
@XmlElement(name="dni")
public String getDni() {
	return dni;
}

public void setDni(String dni) {
	this.dni = dni;
}
@XmlElement(name="nom")
public String getNom() {
	return nom;
}

public void setNom(String nom) {
	this.nom = nom;
}
@XmlElement(name="cognom")
public String getCognom() {
	return cognom;
}

public void setCognom(String cognom) {
	this.cognom = cognom;
}
@XmlElement(name="telefon")
public int getTelefon() {
	return telefon;
}

public void setTelefon(int telefon) {
	this.telefon = telefon;
}
@XmlElement(name="correu")
public String getCorreu() {
	return correu;
}

public void setCorreu(String correu) {
	this.correu = correu;
}
@XmlElement(name="num_identificatiu")
public String getNum_identificatiu() {
	return num_identificatiu;
}

public void setNum_identificatiu(String num_identificatiu) {
	this.num_identificatiu = num_identificatiu;
}
@XmlElement(name="minuts")
public int getMinuts() {
	return minuts;
}

public void setMinuts(int minuts) {
	this.minuts = minuts;
}
@XmlElement(name="nomina")
public int getNomina() {
	return nomina;
}

public void setNomina(int nomina) {
	this.nomina = nomina;
}
@XmlElement(name="any")
public int getAny() {
	return any;
}

public void setAny(int any) {
	this.any = any;
}
@XmlElement(name="mes")
public int getMes() {
	return mes;
}

public void setMes(int mes) {
	this.mes = mes;
}

@XmlElement(name="num_nomina")
public String getNum_nomina() {
	return num_nomina;
}

public void setNum_nomina(String num_nomina) {
	this.num_nomina = num_nomina;
}
@XmlElement(name="id_nomina")
public int getId_nomina() {
	return id_nomina;
}

public void setId_nomina(int id_nomina) {
	this.id_nomina = id_nomina;
}
@XmlElement(name="jornades")
public int getJornades() {
	return jornades;
}

public void setJornades(int jornades) {
	this.jornades = jornades;
}



}
