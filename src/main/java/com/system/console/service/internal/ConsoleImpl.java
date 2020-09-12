package com.system.console.service.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.system.console.service.ConsoleService;


@Service(value = "consoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class ConsoleImpl implements ConsoleService {

}
