package com.ase.bytealchemists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Byte Alchemists application.
 *
 * @author Jason
 * @version 1.0
 */
@SpringBootApplication
public class ByteAlchemistsApplication {
  /**
   * The main launcher for the service all it does
   * is make a call to the overridden run method.
   *
   * @param args A {@code String[]} of any potential
   *             runtime arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(ByteAlchemistsApplication.class, args);
  }
}
