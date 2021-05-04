# Java Consumer Example

### **Things you'll need before getting started:**

1. You'll need a cluster running in Confluent Cloud with a topic to send data too. In this `README`, I'll be referencing a topic named `colors`. 
1. You'll need to copy the client configuration properties from the **Clients** page in your cluster. This should give you a copy-able set of properties that you'll to use.
    - You should paste the specific values into the placeholders seen in `setup.properties`, or alternatively paste over the entire thing.
    
### **Using the Consumer**

- Either build the image yourself using the provided `Dockerfile`, or reference the pre-built image `zachhamilton/consumer-example`. This `README` will use the latter. 

- Reference the following table to see the configuration parameters that can be passed to the consumer as environment variables.
  
  | Environment Variable | Default Value        | Required? |
  |----------------------|----------------------|:---------:|
  | `GROUP_ID`           | `"consumer_example"` | N         |
  | `TOPIC`              | `null`               | Y         |
  | `PARTITION`          | `null`               | N         |
  | `AUTO_OFFSET_RESET`  | `"earliest"`         | N         |

- You'll need to volume mount the consumer container to the directory where `setup.properties` is found in order for the consumer to use the configuration.
    ```bash
    ...
    -v $(pwd)/:/config/:ro
    ...
    ```
- The following is an example of running consumer containers, specifically subscribing them to partitions 0 and 1 of a topic named `colors`. 
    - ConsumerA:
      ```bash
      docker run --rm --it \
      -e GROUP_ID="grouped" \
      -e TOPIC="colors" \
      -e AUTO_OFFSET_RESET="latest" \
      -e PARTITION="0" \
      -v $(pwd)/:/config/:ro \
      zachhamilton/consumer-example  
      ```
    - ConsumerB:
      ```bash
      docker run --rm -it \
      -e GROUP_ID="grouped" \
      -e TOPIC="colors" \
      -e AUTO_OFFSET_RESET="latest" \
      -e PARTITION="1" \
      -v $(pwd)/:/config/:ro \
      zachhamilton/consumer-example  
      ```
- The following example shows an instance of a single consumer container without a specified partition assignment. Any additional consumer containers added to the group will have their partition assignment balanced. 
    - Consumer:
      ```bash
      docker run --rm -it \
      -e GROUP_ID="single" \
      -e TOPIC="colors" \
      -e AUTO_OFFSET_RESET="latest" \
      -v $(pwd)/:/config/:ro \
      zachhamilton/consumer-example
      ```