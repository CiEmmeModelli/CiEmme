/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.test.spi.event.suite;

import org.jboss.arquillian.core.spi.event.Event;

/**
 * Base for events fired in the Suite execution cycle.
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class SuiteEvent implements Event {
    public SuiteEvent() {
    // Questo metodo è vuoto perché è inteso per l'override nelle sottoclassi.
    // Se non è necessario alcun comportamento specifico, è possibile mantenerlo vuoto.
    // Se si ha intenzione di implementare comportamenti personalizzati, si dovrebbe sovrascrivere questo metodo nelle sottoclassi.
    // Se non è previsto alcun comportamento specifico per le sottoclassi, potremmo anche lanciare un'UnsupportedOperationException qui.
    }
}
