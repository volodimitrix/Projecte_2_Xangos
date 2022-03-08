from flask import Flask,render_template,request
import psycopg2
import subprocess
import sys


app = Flask(__name__)

@app.route("/")
def inici():
	return render_template("main.html")	

@app.route("/formulari_resposta",methods=['GET'])
def execucioResposta():
		nom = request.args.get('nomClient')
		cognom = request.args.get('cognomClient')
		adreca = request.args.get('adrecaClient')
		pais = request.args.get('paisClient')
		tipus = request.args.get('tipusClient')
		print("variables1--->",nom,cognom,adreca,pais,tipus)
		print("Insertar",nom,cognom,adreca,pais,tipus)
		insertPr(nom,cognom,adreca,pais,tipus)
		return render_template('resposta.html',nom=nom,cognom=cognom,adreca=adreca,pais=pais,tipus=tipus)
		
def insertPr(nom,cognom,adreca,pais,tipus):		
	try:
		connection = psycopg2.connect(user="postgres",
									password="postgres",
									host="127.0.0.1",
									port="5432",
									database="m102")
		cursor = connection.cursor()
		print("conexio")
		print("variables2---->",nom,cognom,adreca,pais,tipus)
		postgres_insert_query = """ INSERT INTO clients (nom, cognom, adreça, pais, tipus) VALUES (%s,%s,%s,%s,%s)"""
		insertar = (nom,cognom,adreca,pais,tipus)
		cursor.execute(postgres_insert_query, insertar)	

		connection.commit()
		count = cursor.rowcount
		print(count, "Valors inserits a client")
	except (Exception, psycopg2.Error) as error:
		print("No sha pogut insertar" ,error)
	finally:
		if connection:
			cursor.close()
			connection.close()
			print("se fue")

@app.route('/pagina2')
def pagina2():
	conexion1 = psycopg2.connect(database="m102", user="postgres", password="postgres", port="5432", host="127.0.0.1")
	cursor1=conexion1.cursor()
	cursor1.execute("select * from clients")
	clients = cursor1.fetchall()
	for fila in cursor1:
   	 print(fila)
	return render_template('pagina2.html',clients=clients)	
	conexion1.close()	
		
@app.route('/pagina3')
def pagina3():
	return render_template('formulari.html')		


@app.route("/csvClients")
def crear():
	query = """
		select *
		from clients
	"""
	conn = psycopg2.connect("dbname = 'm102' user = 'postgres' host = '127.0.0.1' password = 'postgres'")
	cur = conn.cursor()

	outputquery = "COPY ({0}) TO STDOUT WITH CSV HEADER".format(query)

	with open('/home/dam/Desktop/clients.csv', 'w') as f:
		cur.copy_expert(outputquery, f)

	conn.close()
	return render_template("main.html")

@app.route("/csvProductes")
def crearP():
	query1 = """
		select *
		from productes
	"""
	conn4 = psycopg2.connect("dbname = 'm102' user = 'postgres' host = '127.0.0.1' password = 'postgres'")
	cur2 = conn4.cursor()

	outputquery = "COPY ({0}) TO STDOUT WITH CSV HEADER".format(query1)

	with open('/home/dam/Desktop/productes.csv', 'w') as f:
		cur2.copy_expert(outputquery, f)

	conn4.close()
	return render_template("main.html")

@app.route("/taules")
def taules():
	conexion2 = psycopg2.connect(database="m102", user="postgres", password="postgres", port="5432", host="127.0.0.1")
	cursor2=conexion2.cursor()
	cursor2.execute("select * from clients")
	clients = cursor2.fetchall()
	for fila in cursor2:
   	 print(fila)
	return render_template("prova.html",clients=clients)

@app.route("/formulari_productes")
def formproductes():
	return render_template("productesform.html")	

@app.route("/formulari_resposta_prd",methods=['GET'])
def execucioResposta2():
		codi_prod = request.args.get('codiProducte')
		desc_prod = request.args.get('descProducte')
		insertPr2(codi_prod,desc_prod)
		return render_template('main.html')

def insertPr2(codi_prod,desc_prod):
	try:
		connection = psycopg2.connect(user="postgres",
									password="postgres",
									host="127.0.0.1",
									port="5432",
									database="m102")
		cursor = connection.cursor()
		postgres_insert_query = """ INSERT INTO productes (codi_producte,descripcio) VALUES (%s,%s)"""
		insertar2 = (codi_prod,desc_prod)
		cursor.execute(postgres_insert_query, insertar2)	

		connection.commit()
		count = cursor.rowcount
		print(count, "Valors inserits a producte")
	except (Exception, psycopg2.Error) as error:
		print("No sha pogut insertar" ,error)
	finally:
		if connection:
			cursor.close()
			connection.close()
			print("se fue")
#ventes
@app.route("/formulari_ventes")
def formVentes():
	return render_template("formventes.html")

@app.route("/formulari_ventes_res",methods=['GET'])
def execucioResposta3():
		idVenta = request.args.get('idVenta')
		codiProducte2 = request.args.get('codiProducte')
		idClient = request.args.get('idClient')
		insertPr3(idVenta,codiProducte2,idClient)
		return render_template('main.html')

def insertPr3(idVenta,codiProducte2,idClient):
	try:
		connection = psycopg2.connect(user="postgres",
									password="postgres",
									host="127.0.0.1",
									port="5432",
									database="m102")
		cursor = connection.cursor()
		postgres_insert_query = """ INSERT INTO ventes (id_venta,codi_producte,id_client) VALUES (%s,%s,%s)"""
		insertar3 = (idVenta,codiProducte2,idClient)
		cursor.execute(postgres_insert_query, insertar3)	

		connection.commit()
		count = cursor.rowcount
		print(count, "Valors inserits a producte")
	except (Exception, psycopg2.Error) as error:
		print("No sha pogut insertar" ,error)
	finally:
		if connection:
			cursor.close()
			connection.close()
			print("se fue")

@app.route('/selectProducte')
def select():
        
    conn1 = psycopg2.connect(host="127.0.0.1", port="5432", database="m102", user="postgres", password="postgres")
    cursor1 = conn1.cursor()
    cursor1.execute("SELECT * FROM productes")
    
    productes = cursor1.fetchall()
    for fila in cursor1:
        print(fila)

    return render_template('taulaproductes.html', prd = productes)
    conn1.close()

@app.route('/selectVentes')
def selectVentes():
        
    conn1 = psycopg2.connect(host="127.0.0.1", port="5432", database="m102", user="postgres", password="postgres")
    cursor1 = conn1.cursor()
    cursor1.execute("SELECT * FROM ventes")
    
    ventes = cursor1.fetchall()
    for fila in cursor1:
        print(fila)

    return render_template('taulaventes.html', ventes = ventes)
    conn1.close()






if __name__ == '__main__':
	app.run(debug=True)

