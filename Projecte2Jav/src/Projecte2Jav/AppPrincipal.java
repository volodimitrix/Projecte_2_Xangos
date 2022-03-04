package Projecte2Jav;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.State;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AppPrincipal {
	static Semaphore inici = new Semaphore(0);

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, JAXBException, IOException, InterruptedException {
		Class.forName("org.postgresql.Driver");
		CrearXMLTreballador();

		// SEMAFOR , Primer es crearan els directoris, per després poder ficar el PDF
		Thread p1 = new crearSemDirectoris();
		Thread a1 = new crearSemPdf();
		p1.start();
		a1.start();
		p1.join();
		a1.join();
		System.out.println("PDF generat correctament");

	}

	public static void crearDirectoris() throws SQLException {
		System.out.println("--------------------creant directoris----------------");
		Connection conexion = DriverManager.getConnection("jdbc:postgresql://192.168.1.207:5432/odoo", "oriadix","Fat/3232");

		try {
			Statement con = conexion.createStatement();
			System.out.println("Conexió creada");

		} catch (SQLException e) {
			System.out.println(e);
		}

		try {
			Statement pr2 = conexion.createStatement();
			String sql2 = "select treballadors.dni, nomina.num_nomina, nomina.year, nomina.mes  FROM treballadors, entrega,nomina WHERE treballadors.id_treballador = entrega.id_treballador AND entrega.id_nomina = nomina.id_nomina and num_nomina IS NOT NULL;";
			ResultSet rst2 = pr2.executeQuery(sql2);
			while (rst2.next()) {
				String dni = rst2.getString("dni");
				String numnom = rst2.getString("num_nomina");
				String any = rst2.getString("year");
				String mes = rst2.getString("mes");
				System.out.println(dni + " " + numnom + " " + any + " " + mes);
//				String nomArxiu = "C:\\Prova\\" + numnom + "\\" + dni + "\\" + any + "\\" + mes;
				String nomArxiu = "\\\\192.168.1.207\\samba-compartir\\"+ numnom + "\\" + dni + "\\" + any + "\\" + mes;
				Path path = Paths.get(nomArxiu);
				// File file = new File("C:\\Prova\\"+numnom+"\\"+dni+"\\"+any+"\\"+mes);
				// System.out.println(file);

				if (!Files.exists(path)) {
					Files.createDirectories(path);
					System.out.println("Directori creat" + nomArxiu);
				} else {
					System.out.println("Ja existeix " + nomArxiu);
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------------------------------------");

	}

	public static void CrearXMLTreballador() throws JAXBException, SQLException, IOException {
		System.out.println("-----------------------CREAR XML----------------------");

		int i = 0;
		JAXBContext context = JAXBContext.newInstance(Entrega.class);
		Marshaller marshaller = context.createMarshaller();
		Entrega entrega = new Entrega();
		entrega.setNomTreballador("Prova entrega");
		ArrayList<Treballador> treballadors = new ArrayList();
		Connection conXML = DriverManager.getConnection("jdbc:postgresql://192.168.1.207:5432/odoo", "oriadix","Fat/3232");

		Statement pr4 = conXML.createStatement();
		String sql5 = "SELECT treballadors.id_treballador, treballadors.dni, treballadors.nom,treballadors.cognom,treballadors.telefon,treballadors.correu,treballadors.num_identificatiu,nomina.minuts,nomina.year,nomina.mes,nomina.num_nomina,  entrega.id_nomina, nomina.jornades FROM treballadors, entrega, nomina  WHERE treballadors.id_treballador = entrega.id_treballador AND entrega.id_nomina = nomina.id_nomina;";
		ResultSet rst4 = pr4.executeQuery(sql5);
		while (rst4.next()) {
			i++;
			Treballador treballador = new Treballador();
			int id_treballador = rst4.getInt("id_treballador");
			String dni = rst4.getString("dni");
			String nom = rst4.getString("nom");
			String cognom = rst4.getString("cognom");
			int telefon = rst4.getInt("telefon");
			String correu = rst4.getString("correu");
			String num_identificatiu = rst4.getString("num_identificatiu");
			String num_nomina = rst4.getString("num_nomina");
			int minuts = rst4.getInt("minuts");
			int any = rst4.getInt("year");
			int mes = rst4.getInt("mes");
			int id_nomina = rst4.getInt("id_nomina");
			int jornades = rst4.getInt("jornades");
					
					
			System.out.println(id_treballador + " " + dni + " " + nom + " " + cognom + " " + telefon + " " + correu
					+ " " + num_identificatiu + " " + minuts + " " + any + " " + mes + " " + num_nomina);

			treballador.setId_treballador(id_treballador);
			treballador.setDni(dni);
			treballador.setNom(nom);
			treballador.setCognom(cognom);
			treballador.setTelefon(telefon);
			treballador.setCorreu(correu);
			treballador.setNum_identificatiu(num_identificatiu);
			treballador.setMinuts(minuts);
			treballador.setAny(any);
			treballador.setMes(mes);
			treballador.setNum_nomina(num_nomina);
			treballador.setId_nomina(id_nomina);
			treballador.setJornades(jornades);
			treballadors.add(treballador);

			entrega.setTreballadors(treballadors);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// marshaller.marshal(entrega, System.out);
			marshaller.marshal(entrega, new FileWriter("\\\\192.168.1.207\\samba-compartir\\xml\\"+"Treballador" + i + ".xml"));
			//marshaller.marshal(entrega, new FileWriter("Treballador" + i + ".xml"));

			
		}
		System.out.println("--------------------------------------------------");
	}

	public static void LlegirXML() throws JAXBException, SQLException {
		JAXBContext context = JAXBContext.newInstance(Entrega.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		int i = 0;
		Connection con2 = DriverManager.getConnection("jdbc:postgresql://192.168.1.207:5432/odoo", "oriadix",
				"Fat/3232");
		Statement pr4 = con2.createStatement();
		String sql4 = "select entrega.distancia,nomina.minuts,nomina.num_identificatiu,nomina.salari FROM treballadors, entrega,nomina WHERE treballadors.id_treballador = entrega.id_treballador AND entrega.id_nomina = nomina.id_nomina";
		ResultSet rst4 = pr4.executeQuery(sql4);
		int a = 0;
		while (rst4.next()) {
			i++;
			String nomXml = "\\\\192.168.1.207\\samba-compartir\\xml\\"+"Treballador" + i + ".xml";
		//	String nomXml = "Treballador" + i + ".xml";
			System.out.println(nomXml);
			Entrega entrega = (Entrega) unmarshaller.unmarshal(new File(nomXml));
			ArrayList<Treballador> treball = entrega.getTreballadors();
			for (Treballador t : treball) {
				a++;
				String dni = t.getDni();
				int id_treballador = t.getId_treballador();
				String nom = t.getNom();
				String cognom = t.getCognom();
				int telefon = t.getTelefon();
				String correu = t.getCorreu();
				String num_identificatiu = t.getNum_identificatiu();
				int minuts = t.getMinuts();
				int nomina = t.getNomina();
				int preuxminuts = 4;
				int tot = minuts * preuxminuts;
				int any = t.getAny();
				int mes = t.getMes();
				int id_nomina = t.getId_nomina();
				int jornades = t.getJornades();
				String num_nomina = t.getNum_nomina();
				System.out.println("Minuts totals: " + minuts + " per preu/min " + preuxminuts + " = " + tot);

				Document document = new Document();
				int x = 0;

				try {
//					String path = "C:/Prova/" + num_nomina + "/" + dni + "/" + any + "/" + mes;
//					String FILE_NAME = path + "/" + dni + "_" + mes + "_" + any +"_"+jornades+".pdf";

					String path = "\\\\192.168.1.207\\samba-compartir\\"+num_nomina+"\\"+dni+"\\"+any+"\\"+mes;
					String FILE_NAME = path + "\\" + dni + "_" + mes + "_" + any +"_"+jornades+".pdf";
					System.out.println(FILE_NAME);
						
					
					PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));
				 
					document.open();
					Paragraph paragraphHello = new Paragraph();
					paragraphHello.add("Xangos Delivery");
					paragraphHello.setAlignment(Element.ALIGN_CENTER);

					Image imagen = Image.getInstance("C:/img/log.png");
					imagen.scaleAbsoluteWidth(75);
					imagen.scaleAbsoluteHeight(75);
					document.add(imagen);
					document.add(new Phrase("\n"));

					document.add(Chunk.NEWLINE);

					PdfPTable taula = new PdfPTable(3);
					taula.addCell("Empresa");
					taula.addCell("Num identificatiu");
					taula.addCell("Periode");
					taula.addCell("Xangos Delivery");
					taula.addCell(num_nomina);
					taula.addCell("31-" + mes + "-" + any);
					document.add(taula);
					document.add(Chunk.NEWLINE);

					java.util.List<Element> paragraphList = new ArrayList<>();


					Paragraph p3 = new Paragraph();
					// p3.setFont(f);
					p3.addAll(paragraphList);
					document.add(new Phrase("\n"));
					document.add(Chunk.NEWLINE);
					p3.add("Nom i Cognoms: " + nom + " " + cognom);
					document.add(p3);

					Paragraph p4 = new Paragraph();
					p4.add("Telefon: " + telefon);
					document.add(p4);

					Paragraph p5 = new Paragraph();
					p5.add("Correu: " + correu);
					document.add(p5);

					Paragraph p6 = new Paragraph();
					p6.setSpacingBefore(20);
					document.add(p6);

					Paragraph p7 = new Paragraph();
					p7.add("Temps treballats: " + minuts);
					p7.setAlignment(Element.ALIGN_RIGHT);
					document.add(p7);

					Paragraph p8 = new Paragraph();
					p8.add("Salari: " + tot);
					p8.setAlignment(Element.ALIGN_RIGHT);
					document.add(p8);
					//con2.close();	
					document.close();

				} catch (FileNotFoundException | DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class crearSemDirectoris extends Thread {
		public void run() {
			try {
				crearDirectoris();
				System.out.println("Directoris creats");
				inici.release();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static class crearSemPdf extends Thread {
		public void run() {

			try {
				inici.acquire();
				LlegirXML(); // llegira els xml generats, i crea un pdf per cada que hagi
				System.out.println("crearSemPDF ok ");

			} catch (InterruptedException | JAXBException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	

}