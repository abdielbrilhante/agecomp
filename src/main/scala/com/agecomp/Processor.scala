package com.agecomp

abstract class Processor(val entityManager: EntityManager) {
  def run
}
