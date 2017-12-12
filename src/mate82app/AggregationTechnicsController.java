/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mate82app;

//<editor-fold defaultstate="collapsed" desc="----- IMPORTAÇÕES -----">

import Algorithms.AlgorithmsFactory;
import Algorithms.AlgorithmsType;

import Algorithms.AverageWithoutMisery;
import Algorithms.LeastMisery;
import Algorithms.Multiplicative;
import Algorithms.MostPleasure;
import Algorithms.BordaCount;
import Algorithms.Summarized;


import Data.Data;
import Data.User;
import Data.Vote;
import Data.Structure;
import java.awt.TextField;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

    //</editor-fold>   

public class AggregationTechnicsController implements Initializable {
    
    //<editor-fold defaultstate="collapsed" desc="----- INICIALIZAÇÕES -----">
    
    @FXML
    public Button   btRun;
    public Button   btPopulateBD;
    public Button   btGetDataBD;
    
    public ComboBox  cmbAlgorithms;
    
    
    @FXML
    public TableView<Structure> tableview ;
    public TableColumn<Structure,String> C1 ;
    public TableColumn<Structure,Double> C2;
    public TableColumn<Structure,Double> C3;
    public TableColumn<Structure,Double> C4;
    public TableColumn<Structure,Double> C5;
    public TableColumn<Structure,Double> C6;
    public TableColumn<Structure,Double> C7;
    public TableColumn<Structure,Double> C8;
    
    @FXML
    private Label label;
    public javafx.scene.control.TextField txthroas;
    public javafx.scene.control.TextField txtVotos;

    private List<User> ListData;
    @FXML
    private Label lblResult;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        C1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        C2.setCellValueFactory(new PropertyValueFactory<>("T22"));
        C3.setCellValueFactory(new PropertyValueFactory<>("T23"));
        C4.setCellValueFactory(new PropertyValueFactory<>("T24"));
        C5.setCellValueFactory(new PropertyValueFactory<>("T25"));
        C6.setCellValueFactory(new PropertyValueFactory<>("T26"));
        C7.setCellValueFactory(new PropertyValueFactory<>("T27"));
        C8.setCellValueFactory(new PropertyValueFactory<>("T28"));
        
        for (AlgorithmsType.Type T : AlgorithmsType.Type.values()) {
             cmbAlgorithms.getItems().add(T.ordinal(), T.toString());
        }
         
    }    

    //</editor-fold>      
    
    //<editor-fold defaultstate="collapsed" desc="----- FUNÇÕES DA TELA -----">
    
    @FXML
    private void btPopulateBD_Click(ActionEvent event) throws SQLException, ParseException {
        Data e = new Data();
        e.putMySQLSyntheticData();
    }
    
     @FXML
    private void btGetDataBD_Click(ActionEvent event) throws SQLException, ParseException {
        
         Data e = new Data();
         ListData =  e.getMySQLSyntheticData(Integer.valueOf(txthroas.getText()));
         List<Structure>ListView;
         ListView = ConvertToViewGrid(ListData);
        
       /*Cria tabela  */  
       ObservableList<Structure> items = FXCollections.observableArrayList();
       items.setAll(ListView);
       tableview.setItems(items);
    }
    
    @FXML
    private void btRun_Click(ActionEvent event) {
    
        AlgorithmsFactory f = new AlgorithmsFactory();
        List<Structure> temp= null;
        Vote R;
        
        switch (cmbAlgorithms.getSelectionModel().getSelectedIndex()) {
            
            case 0:
                LeastMisery LM;
                LM = (LeastMisery) f.getAlgorithm(AlgorithmsType.Type.LeastMisery);
                temp = ConvertToViewGrid(LM.GetAll(ListData));
                
                R = LM.GetResult(ListData);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;
                
            case 1:
                
                AverageWithoutMisery AWM;
                AWM = (AverageWithoutMisery) f.getAlgorithm(AlgorithmsType.Type.AverageWithoutMisery);
                temp = ConvertToViewGrid(AWM.GetAll(ListData,1));
                
                R = AWM.GetResult(ListData,1);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;
            
             case 2:
                Multiplicative M;
                M = (Multiplicative) f.getAlgorithm(AlgorithmsType.Type.Multiplicative);
                temp = ConvertToViewGrid(M.GetAll(ListData));
                
                R = M.GetResult(ListData);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;
            
             case 3:
                MostPleasure MP;
                MP = (MostPleasure) f.getAlgorithm(AlgorithmsType.Type.MostPleasure);
                temp = ConvertToViewGrid(MP.GetAll(ListData));
                
                R = MP.GetResult(ListData);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;
                
             case 4:
                BordaCount BC;
                BC = (BordaCount) f.getAlgorithm(AlgorithmsType.Type.BorderCount);
                temp = ConvertToViewGrid(BC.GetAll(ListData));
                
                R = BC.GetResult(ListData);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;     
                
             case 5:
                Summarized SUM;
                SUM = (Summarized) f.getAlgorithm(AlgorithmsType.Type.Summarized);
                temp = ConvertToViewGrid(SUM.GetAll(ListData,null));
                
                R = SUM.GetResult(ListData, null);
                lblResult.setText(String.valueOf(R.getScaleValue()));
                
                break;                   
        }
        
       ObservableList<Structure> items = FXCollections.observableArrayList();
       items.setAll(temp);
       tableview.setItems(items);
        
        
    }
     
    //</editor-fold>      
    
    //<editor-fold defaultstate="collapsed" desc="----- SUPORTE -----">
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<Structure> ConvertToViewGrid(List<User> list){
        
         /* Rotina criada apenas para ~visdualização dos dados */
         List<Structure>  ListView = new ArrayList<>();
         Structure s;
         
         s = new Structure();    
         s.setName("");
         s.setT22(0.0);
         
         for (User l : list) {
             
            s = new Structure();    
            s.setName(l.getName());

            List<Vote> vl;
            vl = l.getVote();
            
            
            s.setT22(vl.get(0).getVote());
            s.setT23(vl.get(1).getVote());
            s.setT24(vl.get(2).getVote());
            s.setT25(vl.get(3).getVote());
            s.setT26(vl.get(4).getVote());
            s.setT27(vl.get(5).getVote());
            s.setT28(vl.get(6).getVote());
            ListView.add(s);

         }
         return ListView;
        
    }
    
    //</editor-fold>  

    
}

    //<editor-fold defaultstate="collapsed" desc="----- TRASH -----">


//    @FXML
//    private void btSimulaVotacao_Click(ActionEvent event) throws SQLException {
//        
////       System.out.println(txtUsers.getText());
////        
////       OLD_Data3 pop = new OLD_Data3();
////       ListData = pop.GenerateData(Integer.valueOf(txtUsers.getText()),Integer.valueOf(txtVotos.getText()));
////       
////       /* Carrega Tabela */
////       ObservableList<Data> items = FXCollections.observableArrayList();
////       items.setAll(ListData);
////       tableview.setItems(items);
//    }
//    
//    @FXML
//    private void btSumarizaDados_Click(ActionEvent event) throws SQLException {
//        
////       List<Data3> d = ListData;
////       List<Data3> oDistinctUsers = ListData.stream().filter(distinctByKey(p -> p.getName())).collect(Collectors.toList());
////
////       OLD_Data3 oVote= null;
////       ListDataSumarize = new  ArrayList<>();
////
////       Double T23=0.0,T24=0.0,T25=0.0,T26=0.0,T27=0.0,T28=0.0;
////       
////        for(OLD_Data3 user: oDistinctUsers){ 
////            
////            oVote= new OLD_Data3();
////            oVote.setName(user.getName());
////            
////            for(OLD_Data3 likes: ListData){ 
////                
////                System.out.println(user.getName());
////                System.out.println(likes.getName());
////                
////                if (user.getName().equals(likes.getName()) ) {
////                    T23 = T23 + likes.getT23();
////                    T24 = T24 + likes.getT24();
////                    T25 = T25 + likes.getT25();
////                    T26 = T26 + likes.getT26();
////                    T27 = T27 + likes.getT27();
////                    T28 = T28 + likes.getT28();
////                }
////            }
////            oVote.setT23(T23);
////            oVote.setT24(T24);
////            oVote.setT25(T25);
////            oVote.setT26(T26);
////            oVote.setT27(T27);
////            oVote.setT28(T28);
////            ListDataSumarize.add(oVote);
////            T23=0.0;T24=0.0;T25=0.0;T26=0.0;T27=0.0;T28=0.0;
////             
////        }
////
////        ObservableList<Data3> items = FXCollections.observableArrayList();
////        items.setAll(ListDataSumarize);
////        tableview.setItems(items);
//    }






      
         
//         
//         /* Load LInhas */
//         for (User row : ListData) {
//            
//             s = new Structure();    
//             s.setName(row.getName());
//             
////             List<Vote> vl;
////             vl = row.getVote();
//             
//             
//            
//             
//             Field[] fields = Structure.class.getDeclaredFields();
//             for (Field field : fields) {
//                 
//                 for (Vote v : row.getVote()) {
//                     
//                     System.out.println(field.getName().toString());
//                     System.out.println(String.valueOf(v.getScaleValue()));
//                     
//                     if (field.getName().toString() == String.valueOf(v.getScaleValue())) {
//                          System.out.println(v.getVote());
//                          
//                                                    
//                     }
//                 }
//                 
//                 
//                 
//                 
////                 if (field.getName().toString() == String.valueOf(((Vote) row.getVote()).getScaleValue())) {
////                       System.out.println(field.getName().toString());
////
////                 }
//                 
//                 
//               
//             } 
//             
//             
//             
//             
//             //s.setT23(((Vote) row.getVote()).getVote());
//             
////             ListView.add(s);
//         }

//</editor-fold>     