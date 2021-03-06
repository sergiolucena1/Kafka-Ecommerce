package br.com.alura.ecomerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

//escutar meus topicos
public class LogService {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Pattern.compile("ECOMMERCE.*"));                                     //Escuta diversos topicos que seguem a expressao ECOMMERCE(qualquer coisa q com3ece com eccomerce)
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));                                  // perguntando ao consumer se tem mensagem por 100 milisecs
            if (!records.isEmpty()) {                                                                   //se os registros estão vazios
                System.out.println("Encontrei" + records.count() + "registros");
                for (var record : records) {
                    System.out.println("______________________________________________");
                    System.out.println("LOG: " + record.topic());
                    System.out.println(record.key());
                    System.out.println(record.value());                     //valor da mensagem
                    System.out.println(record.partition());                 //particao q foi enviada
                    System.out.println(record.offset());
                }
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());        // transformar a chave de byte pra string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());              //serviço de log
        return properties;
    }

}
