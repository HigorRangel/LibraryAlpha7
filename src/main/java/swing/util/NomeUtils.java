package swing.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NomeUtils {

   //TODO adicionar um método Util para remover espaços excessivos

    //TODO verificar se precisa apagar o método abaixo
//
//    /**
//     *  Formata o nome do autor para o padrão "A. B. C. Sobrenome ou "A. Sobrenome"
//     * @param nomes Lista de nomes a serem formatados
//     * @return String com os nomes formatados
//     */
//    public static String abreviarNomes(List<String> nomes) {
//        List<String> autoresFormatados = new ArrayList<>();
//        for (String nome : nomes) {
//            String[] partes = nome.split(" ");
//            StringBuilder nomeFormatado = new StringBuilder();
//            String primeiroNome = partes[0];
//            String ultimoNome = partes[partes.length - 1];
//            List<String> nomesIntermediarios = Arrays.asList(partes).subList(1, partes.length - 1);
//            String nomesIntermediarioString = nomesIntermediarios.stream()
//                    .map(s -> s.substring(0, 1).toUpperCase() + ".")
//                    .collect(Collectors.joining(" "));
//
//            nomeFormatado.append(primeiroNome.charAt(0))
//                    .append(" ")
//                    .append(nomesIntermediarioString)
//                    .append(" ")
//                    .append(ultimoNome);
//
//            autoresFormatados.add(nomeFormatado.toString());
//        }
//
//        return String.join(", ", autoresFormatados);
//    }
}
