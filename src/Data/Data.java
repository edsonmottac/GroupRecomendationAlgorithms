package Data;

import Data.MySql.MySQLObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Data {

    
    /* Carrega dados fictícios no Mysql */
    public void putMySQLSyntheticData() throws SQLException, ParseException {
        
        Statement s =  MySQLObject.getConexaoMySQL().createStatement(); 
        String sqlQuery =  " DELETE FROM mate84.users  " ;
        s.executeUpdate(sqlQuery);
        
        
        
                
        LocalDateTime timeStart = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth() , LocalDate.now().getDayOfMonth(), 9, 00, 00);
        LocalDateTime timeEnd = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 22, 00, 00);
        Duration oneHours = Duration.between(timeStart, timeEnd);
        
        long qtHoras = oneHours.toHours();
        Integer qtVotesByHour = 10;
        Integer intervalo = (60/qtVotesByHour); /* Partition of Synthetic Load: Intervalo de tempo entre os votos (se a quantidade de votos definida é 10 60/10 dará uma média de um voto a cada 6min) */
        Integer qtUsers = 4;
        Integer rating=5;
              
        /* Fields */
        LocalDateTime datetime = timeStart ;
        double value;
        double scale;
        String sensor_type = "";
        String unit="";
        String userid;
         
        /* Support */
        Integer rangetime;
        Random rn = new Random();
        Integer count=0;
        
        /* Generate Synthetic Data to MySql */
        for (int hour=0;hour<=qtHoras;hour++) {
            
            for (int v=0;v<=qtVotesByHour;v++) {
                count+=1;
                
                rangetime= (rn.nextInt(intervalo - 1 + 1) + 1);
                datetime = LocalDateTime.of(datetime.toLocalDate(), datetime.toLocalTime()).plusMinutes(rangetime);
                value = rn.nextInt((28 - 22) + 1) + 22;
                scale = (rn.nextInt(rating - 1 + 1) + 1);
                sensor_type = "temperature";
                unit="celsius";
                userid =  "User" + (rn.nextInt(qtUsers - 1 + 1) + 1);
                
                sqlQuery="INSERT INTO mate84.users VALUES(" + count + ",'" + Timestamp.valueOf(datetime) + "'," + scale + "," + value + ",'" + sensor_type + "','" + unit + "','"  + userid + "')";
                System.out.println(sqlQuery);
                s.executeUpdate(sqlQuery);
                
            }
        
        }
    }
    
    /* Ontem os dados do Mysql */
    public List<User> getMySQLSyntheticData(Integer ultimas_horas) throws SQLException {
        
        /* Recebe dados do Banco */
        Statement sgeral =  MySQLObject.getConexaoMySQL().createStatement(); 
        ResultSet rsTemp;
        
        Statement sUserSelc =  MySQLObject.getConexaoMySQL().createStatement(); 
        //String sqlQuery =  " SELECT CONVERT(time,DATETIME) AS time, scale, value, userid  FROM mate84.users WHERE CONVERT(time,DATETIME) BETWEEN DATE_ADD(CONVERT(NOW(),DATETIME), INTERVAL -1 HOUR) AND CONVERT(time,DATETIME)  ORDER BY  CONVERT(time,DATETIME) DESC " ;
        ResultSet rsUsersByLastHour; //= sUserSelc.executeQuery(sqlQuery);

        Statement stmscale =  MySQLObject.getConexaoMySQL().createStatement(); 
        String sqlQuery =  " SELECT DISTINCT value FROM mate84.users ORDER BY value " ; /* Valores das medidas (teperatura) */
        ResultSet rsValue = stmscale.executeQuery(sqlQuery);

        Statement stmuser =  MySQLObject.getConexaoMySQL().createStatement(); 
        sqlQuery =  " SELECT DISTINCT userid FROM mate84.users " ;
        ResultSet rsUsers = stmuser.executeQuery(sqlQuery);

        boolean hasvoted = false;
        Vote vote;
        List<Vote> ListVote = new ArrayList<>();
        User u;
        List<User> ListUsers = new ArrayList<>();

        while (rsUsers.next()) {   /* Usuário X */
            
            ListVote = new ArrayList<>();
            
            while (rsValue.next()) { /* Em um dado item da escala Y - Itera sobre as medidas */

                /* Consulta horas do usuário seleiconado */
                sqlQuery =    " SELECT CONVERT(time,DATETIME) AS time, scale, value, userid  FROM mate84.users  "
                            + " WHERE CONVERT(time,DATETIME) BETWEEN DATE_ADD(CONVERT(NOW(),DATETIME), INTERVAL " + (ultimas_horas * (-1)) + " HOUR) AND CONVERT(time,DATETIME) "
                            + " AND userid = '" + rsUsers.getString("userid") + "' "
                            + " AND value = "  + rsValue.getString("value") + " "
                            + " ORDER BY  CONVERT(time,DATETIME) DESC " ;
                System.out.println(sqlQuery);
                rsUsersByLastHour = sUserSelc.executeQuery(sqlQuery);
                
                while (rsUsersByLastHour.next()) { /* Irata sobre os registro das ultimas x horas  */  
                            //System.out.println(rsUsers.getString("userid"));
                            //System.out.println(rsScale.getString("scale"));
                            //System.out.println(rsUsersByLastHour.getString("value"));
                        
                            vote = new Vote();   
                            System.out.println(rsUsersByLastHour.getDouble("scale")); 
                            /*  Aqui há uma mudança: Os valores de escala que no banco são os valores de preferencia, são persistidos aqui como escala = medida (Temperatura A,Temperatura B, etc)  
                                Do mesmo modo, os valores de preferencia são descrito no banco de dados como scale, mas no modelo de objeto são descritos como "value", logo: value = preferencias)
                            */
                            vote.setVote(rsUsersByLastHour.getDouble("scale")); /* Armazena a preferencia (1 a 5) */
                            vote.setScaleValue(rsUsersByLastHour.getDouble("value")); /* armazena o valor da medida (tempratura) */
                            ListVote.add(vote);
                            hasvoted = true;
                            break;
                  
                }
                
                /*  Usuários que não votaram na ultioma hpra mas estão na lista aplicar o 
                    calculo da quantidade de vezes que ele votou em casa ítem e assumir a quantidade como o rating 
                    [min:1 Max: 5]*/
                if (!hasvoted) {
                    
                    //System.out.println(rsUsers.getString("userid"));
                    //System.out.println(rsScale.getString("scale"));
                    //System.out.println(rsUsersByLastHour.getString("value"));
                            
                    //Filtra pelo valor da medida, se a cntagem for 0 o valor rating (scale) será 1, se for (> 0), será a sumarização de no máximo 5 itens  
                    sqlQuery =  " SELECT count(*) total FROM mate84.users WHERE value = " +  rsValue.getString("value")  + " and userid='" + rsUsers.getString("userid") +"'";
                    System.out.println(sqlQuery);
                    rsTemp = sgeral.executeQuery(sqlQuery);
                    if (rsTemp.next()) {
                        //Preenche os votos vazios
                        vote = new Vote();       
                        if (rsTemp.getInt("total") > 0) {
                            vote.setVote((rsTemp.getInt("total")>5) ? 5 : rsTemp.getInt("total"));
                        } else if (rsTemp.getInt("total") == 0) {
                            vote.setVote(1);
                        }
                        vote.setScaleValue(rsValue.getDouble("value"));
                        ListVote.add(vote);                        
                    }
                    
                }
                hasvoted = false;
                rsUsersByLastHour.first();
            }
            
            /* Adiciona Usuáiro */
            u = new User();
            u.setName(rsUsers.getString("userid"));
            u.setVote(ListVote);
            
            ListUsers.add(u);
            rsValue.beforeFirst();
        } 
        return ListUsers;
        
        
        
       
            
                
          
            
            
     
        
//        List<Vote> v = new ArrayList<Vote>();
//        
//        Vote vote;
//        vote = new Vote();
//        vote.setScaleValue(22);
//        vote.setScaleValue(5);
//        v.add(vote);
//              
//        vote = new Vote();
//        vote.setScaleValue(23);
//        vote.setScaleValue(1);
//        v.add(vote);
//
//        vote = new Vote();
//        vote.setScaleValue(24);
//        vote.setScaleValue(2);        
//        v.add(vote);
//        
//        vote = new Vote();
//        vote.setScaleValue(26);
//        vote.setScaleValue(2);                
//        v.add(vote);
//
//        vote = new Vote();
//        vote.setScaleValue(27);
//        vote.setScaleValue(2);                
//        v.add(vote);
//        
//        vote = new Vote();
//        vote.setScaleValue(28);
//        vote.setScaleValue(2);                
//        v.add(vote);
//
//        /* Adiciona nome e votos */
//        User u = new User();
//        u.setName("Usuário1");
//        u.setVote(v);
        
        
        
      

        
        
        
//        Data2 oVote;
//        List<Data2> Votos = new ArrayList<>();
//        
//        Random rn = new Random();
//     
//        for (int row = 1;row<=rates;row++) {
//            oVote= new Data2();
//            oVote.setName("User" +  row);
//        }

     }

    
}
