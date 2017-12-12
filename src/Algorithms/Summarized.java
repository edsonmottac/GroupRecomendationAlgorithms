package Algorithms;

import Data.User;
import Data.Vote;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Summarized extends Algorithms  {
    
    public List<User>GetAll(List<User> list,  String nome_algoritmo ) {

        double calc=0;

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
                System.out.println(v.getVote());
                if (v.getVote() > 0) { calc = calc + ((double) v.getVote());}
            }

            /* Adiciona resultdo da análise a lista de votos */
            vote_temp= new Vote();
            vote_temp.setScaleValue((double) s);
            vote_temp.setVote(calc);
            list_vote_temp.add(vote_temp);
            calc=0;
         }

        
        
        /* Cria um novo item na lista com o reusltado da análise */
        User new_line = new User();
        new_line.setName((nome_algoritmo==null) ? "Sumaized" : nome_algoritmo);
        new_line.setVote(list_vote_temp);
        list.add(new_line);

        return list;

    }
    
    public Vote GetResult(List<User> list,  String nome_algoritmo ) {
        
            User user_filter;
            list = GetAll(list,nome_algoritmo);

            user_filter = list.stream().filter(p-> p.getName().equals((nome_algoritmo==null) ? "Sumaized" : nome_algoritmo)).collect(Collectors.toList()).get(0);
            Object temp = user_filter.getVote().parallelStream().max(Comparator.comparing(p-> ((Vote) p).getVote())).get();
            return ((Vote) temp);

        }
     
    
}
