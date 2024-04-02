import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.wrapper.AgentController;

import java.util.Random;

public class Aspirador extends Agent {

    private enum Direcao {
        FRONTAL, TRASEIRA, ESQUERDA, DIREITA
    }

    private boolean sensorFrontal;
    private boolean sensorEsquerdo;
    private boolean sensorDireito;
    private boolean sensorTraseiro;
    private boolean limpezaConcluida;
    private Random random;

    protected void setup() {
        System.out.println("Agente aspirador de pó " + getLocalName() + " está pronto.");

        sensorFrontal = false;
        sensorEsquerdo = false;
        sensorDireito = false;
        sensorTraseiro = false;
        limpezaConcluida = false;
        random = new Random();

        addBehaviour(new ComportamentoAspirador());
    }

    private class ComportamentoAspirador extends CyclicBehaviour {

        public void action() {
            detectarSujeira();
            verificarSensores();
            if (sensorFrontal) {
                System.out.println("Ponto de sujeira detectado à frente pelo agente aspirador " + myAgent.getLocalName());
                mover(Direcao.FRONTAL);
                limpar();
            } else if (sensorEsquerdo) {
                System.out.println("Ponto de sujeira detectado à esquerda pelo agente aspirador " + myAgent.getLocalName());
                mover(Direcao.ESQUERDA);
                limpar();
            } else if (sensorDireito) {
                System.out.println("Ponto de sujeira detectado à direita pelo agente aspirador " + myAgent.getLocalName());
                mover(Direcao.DIREITA);
                limpar();
            } else if (limpezaConcluida) {
                desligarAspirador();
            } else {
                mover(Direcao.TRASEIRA);
            }
        }

        private void detectarSujeira() {

            sensorFrontal = random.nextDouble() < 0.3;
            sensorEsquerdo = random.nextDouble() < 0.3;
            sensorDireito = random.nextDouble() < 0.3;
            sensorTraseiro = random.nextDouble() < 0.3;
        }

        private void limpar() {
            System.out.println("Agente aspirador de pó " + myAgent.getLocalName() + " está limpando ponto de sujeira.");
        }

        private void mover(Direcao direcao) {
            System.out.println("Agente aspirador de pó " + myAgent.getLocalName() + " está se movendo " + direcao);
        }

        private void desligarAspirador() {
            System.out.println("Limpeza concluída. Agente aspirador de pó " + myAgent.getLocalName() + " está sendo desligado.");
            doDelete(); // Desliga o agente
        }

        private void verificarSensores() {
            System.out.println("Sensor frontal: " + (sensorFrontal ? "Ativado" : "Desativado"));
            System.out.println("Sensor esquerdo: " + (sensorEsquerdo ? "Ativado" : "Desativado"));
            System.out.println("Sensor direito: " + (sensorDireito ? "Ativado" : "Desativado"));
        }
    }

    public static void main(String[] args) {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        jade.core.Profile p = new jade.core.ProfileImpl();
        jade.wrapper.AgentContainer container = rt.createMainContainer(p);
        try {
            AgentController agent = container.createNewAgent("aspiradorDePo", "AspiradorDePo", null);
            agent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}