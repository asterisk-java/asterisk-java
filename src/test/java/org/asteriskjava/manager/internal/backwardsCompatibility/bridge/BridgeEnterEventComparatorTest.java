/*
 *  Copyright 2018 Marcin Turek
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.internal.backwardsCompatibility.bridge;

import org.asteriskjava.manager.event.BridgeEnterEvent;
import org.junit.Assert;
import org.junit.Test;

public class BridgeEnterEventComparatorTest
{
    private BridgeEnterEventComparator bridgeEnterEventComparator = new BridgeEnterEventComparator();

    @Test
    public void withoutPrefix_shouldId2BeGreater()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590350.29");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldId1BeGreater()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590350.29");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldId2BeGreaterForSameSerials()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590350.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldId1BeGreaterForSameSerials()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590350.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldId2BeGreaterForSameEpoch()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590353.29");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldId1BeGreaterForSameEpoch()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590353.29");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withoutPrefix_shouldIdsBeEqual()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(0, compareResult);
    }

    @Test
    public void withPrefix_shouldId2BeGreater()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590350.29");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withPrefix_shouldId1BeGreater()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590350.29");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withPrefix_shouldId2BeGreaterForSameSerials()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590350.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withPrefix_shouldId1BeGreaterForSameSerials()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590350.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withPrefix_shouldId2BeGreaterForSameEpoch()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590350.29");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590350.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void withPrefix_shouldId1BeGreaterForSameEpoch()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590350.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590350.29");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    @Test
    public void withPrefix_shouldIdsBeEqual()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(0, compareResult);
    }

    @Test
    public void shouldBothIdsDoesNotMatchPattern()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_id_1");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_id_2");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(0, compareResult);
    }

    @Test
    public void shouldId1DoesNotMatchPattern()
    {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_id_1");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_prefix-1515590353.30");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void shouldId2DoesNotMatchPattern() {
        // given
        BridgeEnterEvent bridgeEnterEvent1 = createBridgeEnterEvent("some_prefix-1515590353.30");
        BridgeEnterEvent bridgeEnterEvent2 = createBridgeEnterEvent("some_id_2");

        // when
        int compareResult = bridgeEnterEventComparator.compare(bridgeEnterEvent1, bridgeEnterEvent2);

        // then
        Assert.assertEquals(1, compareResult);
    }

    private static BridgeEnterEvent createBridgeEnterEvent(String uniqueId)
    {
        BridgeEnterEvent bridgeEnterEvent = new BridgeEnterEvent(new Object());
        bridgeEnterEvent.setUniqueId(uniqueId);
        return bridgeEnterEvent;
    }
}