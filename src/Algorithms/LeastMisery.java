
package Algorithms;

import Data.User;
import Data.Vote;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeastMisery extends Algorithms{
    
    public List<User>GetAll(List<User> list) {

        double result=0;
        Integer idx=0;

        List<Vote> votes_filter;
        Vote vote_temp;
        List<Vote> list_vote_temp = new ArrayList<>();

        List<Double> scale = list.stream().map(p -> 
                p.getVote().stream().map(v -> v.getScaleValue()).collect(Collectors.toList())
        ).collect(Collectors.toList()).get(0);
        
        for (Object s : scale) {

            votes_filter = list.stream().map(p -> 
                p.getVote().stream().filter( v -> String.valueOf(v.getScaleValue()).equals(String.valueOf(s))).collect(Collectors.toList()).get(0)
            ).collect(Collectors.toList());
             
            for (Vote v : votes_filter) {
                if (idx==0) {result=v.getVote();}
                System.out.println( " Valor: " + v.getVote() + " Result: " + result);
                if (v.getVote() < result) {
                    result = v.getVote();
                    System.out.println("novo menor valoe: " + result );
                }
            idx+=1;
            }
            idx=0;
            /* Adiciona resultdo da análise a lista de votos */
            vote_temp= new Vote();
            vote_temp.setScaleValue((double) s);
            vote_temp.setVote(result);
            list_vote_temp.add(vote_temp);
            
         }
        
        /* Cria um novo item na lista com o reusltado da análise */
        User new_line = new User();
        new_line.setName("LeastMisery");
        new_line.setVote(list_vote_temp);
        list.add(new_line);

        return list;
        
    }
   
    public Vote GetResult(List<User> list) {
        
        User user_filter;
        list = GetAll(list);

        user_filter = list.stream().filter(p-> p.getName().equals("LeastMisery")).collect(Collectors.toList()).get(0);
        Object temp = user_filter.getVote().parallelStream().min(Comparator.comparing(p-> ((Vote) p).getVote())).get();
        return ((Vote) temp);
        
    }
 
}


//<editor-fold defaultstate="collapsed" desc="----- TRASH -----">





//****** METODOS DE FILTRAGEM E MAPEAMENTO D ELISTAS        
//        List<Players> PlayersByYear  = PlayersList.stream().filter(p -> (p.getAno()== NUM_YEAR.intValue()))
//                                                                    .filter(p -> (p.getLocal_jogo().equals(String.valueOf(LOCAL_DA_PARTIDA))))
//                                                                    .collect(Collectors.toList());

//         List<Object> collect1 = list.stream().map(p -> 
//                p.getVote().stream().filter( v -> v.getScaleValue() == 22).collect(Collectors.toList())
//        ).collect(Collectors.toList());
        
//         List<Object> collect1 = list.stream().map(p -> 
//                p.getVote().stream().map(v1 -> v1.getVote()).iterator().toString()
//        ).collect(Collectors.toList());
//
//        
//         List<Object> collect = list.stream().map(p -> 
//                p.getVote().stream().map(v1 -> v1.getVote()).collect(Collectors.toList())
//        ).collect(Collectors.toList());


//        List<Object> collect1 = list.stream().map(p -> 
//                p.getVote().stream().map( v -> v.getVote()).collect(Collectors.toList())
//        ).collect(Collectors.toList());
//        
//        Object scale1 = list.stream().map(p -> 
//                p.getVote().stream().map(v -> v.getScaleValue()).collect(Collectors.toList())
//        ).collect(Collectors.toList()).get(0);




//******ROTINAS ANTIGAS 
//public List<User>run(List<User> list) {
//        
//        System.out.println("LeastMisery");
//
//        
////        List<Players> PlayersByYear  = PlayersList.stream().filter(p -> (p.getAno()== NUM_YEAR.intValue()))
////                                                                    .filter(p -> (p.getLocal_jogo().equals(String.valueOf(LOCAL_DA_PARTIDA))))
////                                                                    .collect(Collectors.toList());
//
//         List<Object> collect1 = list.stream().map(p -> 
//                p.getVote().stream().filter( v -> v.getScaleValue() == 22).collect(Collectors.toList())
//        ).collect(Collectors.toList());
//        
////         List<Object> collect1 = list.stream().map(p -> 
////                p.getVote().stream().map(v1 -> v1.getVote()).iterator().toString()
////        ).collect(Collectors.toList());
////
////        
////         List<Object> collect = list.stream().map(p -> 
////                p.getVote().stream().map(v1 -> v1.getVote()).collect(Collectors.toList())
////        ).collect(Collectors.toList());
//
//
//        List<Vote> vote_temp = new ArrayList<>();
//        double[] Result = new double[7];
//        Vote tVote;
//        int idx=0;
//        int idxusu=0;
//        
//        double colcount=0.0;
//        for (User u : list) {
//            
//            System.out.println(u.getName());
//            
//            for (Vote v : u.getVote()) {
//                
//               if (idxusu==0) {Result[idx] = v.getVote();}
//                
//                System.out.println("ANTEIROR: " + Result[idx]);
//                System.out.println("NOVO: " + v.getVote());
//                
//                if (v.getVote() < Result[idx]) {
//                    Result[idx] = v.getVote();
//                    vote_temp.clear();
//                }
//                
//                for (int i=0;i<Result.length;i++) {
//                    System.out.println("RESULT: " + Result[i]);
//                    
//                }
//                
//                idx+=1;
//                System.out.println(v.getScaleValue() + " | "  +  v.getVote());
//                colcount+=v.getVote();
//            }
//            idx=0;
//            idxusu+=1;
//        }
//        
//        
//        
//        for (Vote vi : vote_temp) {
//            System.out.println(vi.getScaleValue() + " | " + vi.getVote());
//        }
//        
//        
//
//        return null;
//        
//    }
//    


























//
//
//   public void run(List<Data> oList){
//       
////        for(Object item: oList){ 
////            System.out.println(oList.indexOf(item));
////        }
//
////        Data d = new Data();
////        for(Object item: d.getClass().getFields()){ 
////             System.out.println(item.toString());
////        }
//
////        for (int i=0 ; i<10;i++) {
////            System.out.println(oList.get(i));
////        }
//
//        for(Data item: oList){ 
//            
//           //public java.lang.String Data.Data.Name
//            
//            for(Object p: (new Data()).getClass().getFields()){ 
//                
//                try {
//                   System.out.println(p.toString());
//                   String field;
//                   field = p.toString().replace("public java.lang.", "");
//                   field = field.toString().replace("Data.Data", "");
//                   field = field.toString().replace(" ", "");
//                   field = field.toString().replace("String", "");
//                   field = field.toString().replace("Integer", "");
//                   field = field.toString().replace(".", "");
//                   
//                   System.out.println(item.getClass().getField(field).get(item));
//                   
//                   
//                    
//                } catch (Exception e) {
//                    System.err.print(e);
//                }
//                    
//                 
//                
//            }
//            
//                
//            
//
//        }
//         
//         
//     }
//    

    
    //</editor-fold>      