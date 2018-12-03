import java.util.*;
import java.util.Random;

/*
*******************************************************
**                    UNICESUMAR                     **
*******************************************************
** CURSO ......: Engenharia de Software              **
** DISCIPLINA .: Programação de Sistemas 1           **
** ATIVIDADE ..: MAPA                                **
** ALUNO ......: RA17409065                          **
** DATA .......: 2018.12.07                          **
*******************************************************
*/

public class Mapa {

    public static void main(String args[]) {

        int intVotosQtd = 0;
        int intVotosQtdAux = 0;
        int intVotoAtual = 0;
        int intVencedorNum = 0;
        int intVencedorQtdVotos = 0;
        int intCandidatosQtd = 0;
        int intCandidatosQtdAux = 0;
        int intCandidatosVotos[];

        Random rndVotosGerador = new Random();
        Scanner scnInput = new Scanner(System.in);

        boolean blnNovasInfos = false;
        boolean blnContinuar = false;

        String strNovasInfos = "N";
        String strContinuar = "N";

        do
        {
            System.out.println( "" );
            System.out.println( "" );
            System.out.println( "" );
            System.out.println( "UNICESUMAR - PROGRAMACAO DE SISTEMAS - ALUNO : RA17409065" );
            System.out.println( "****************************************************************************************************" );

            do
            {
                System.out.println( "" );
                System.out.println( "Informe a quantidade de votos [ Qtd > 999 ] : " );
                intVotosQtd = scnInput.nextInt();
                System.out.println( "" );
            } while ( intVotosQtd <= 999 );

            intVotosQtdAux = intVotosQtd;

            do
            {
                System.out.println( "" );
                System.out.println( "Informe a quantidade de candidatos [ Qtd > 1 ]: " );
                intCandidatosQtd = scnInput.nextInt();
                System.out.println( "" );
            }  while ( intCandidatosQtd <= 1 );

            intCandidatosQtdAux = intCandidatosQtd;

            do
            {
                intVotosQtd = 0;
                intVotoAtual = 0;
                intVencedorNum = 0;
                intVencedorQtdVotos = 0;
                intCandidatosQtd = 0;
                intCandidatosVotos = null;

                intVotosQtd = intVotosQtdAux;
                intCandidatosQtd = intCandidatosQtdAux;
                intCandidatosVotos = new int[ intCandidatosQtd ];

                for( int i = 0; i < intVotosQtd; i++ )
                {
                    intVotoAtual = rndVotosGerador.nextInt( intCandidatosQtd );
                    intCandidatosVotos[ intVotoAtual ] += 1;
                }

                for( int i = 0; i < intCandidatosQtd; i++ )
                {
                    if ( intCandidatosVotos[ i ] > intVencedorQtdVotos )
                    {
                        intVencedorNum = i;
                        intVencedorQtdVotos = intCandidatosVotos[ i ];
                    }

                    System.out.println( "Candidato " + ( i + 1 ) + ": " + intCandidatosVotos[ i ] + " votos" );
                }

                System.out.println( "" );
                System.out.println( "O candidato " + ( intVencedorNum + 1 ) + " venceu, com " + intVencedorQtdVotos + " votos!" );
                System.out.println( "" );
                System.out.println( "" );
                System.out.println( "****************************************************************************************************" );
                System.out.println( "" );
                System.out.println( "Deseja realizar uma nova votacao com as mesmas informacoes? [ S / N ]: " );
                strContinuar = scnInput.next();

                if ( strContinuar.toUpperCase().equals( "S" ) )
                {
                    blnContinuar = true;
                }
                else
                {
                    blnContinuar = false;
                }

            } while ( blnContinuar == true );

            System.out.println( "" );
            System.out.println( "Deseja informar Novas Informacoes ou Sair? [ NI / S ]: " );
            strNovasInfos = scnInput.next();

            if ( strNovasInfos.toUpperCase().equals( "NI" ) )
            {
                blnNovasInfos = true;
            }
            else if ( strNovasInfos.toUpperCase().equals( "S" ) )
            {
                blnNovasInfos = false;
            }
            else
            {
                blnNovasInfos = false;
            }

        } while ( blnNovasInfos == true );
    }
}