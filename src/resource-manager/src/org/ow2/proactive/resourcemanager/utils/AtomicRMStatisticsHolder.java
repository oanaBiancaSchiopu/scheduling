package org.ow2.proactive.resourcemanager.utils;

import java.util.concurrent.atomic.AtomicReference;

import org.ow2.proactive.resourcemanager.common.event.RMEvent;
import org.ow2.proactive.resourcemanager.common.event.RMNodeEvent;

/**
 * This class represents a holder of two instances of RMStatistics, two instances 
 * are used because an instance of this class can be potentially manipulated concurrently by
 * multiple threads. In fact, it is a specific case of the single writer and multiple readers
 * situation. The writer updates the statistical data of the first instance and the readers
 * access the statistical data of the second instance. Once the writer has finished 
 * updating it swaps atomically the two instances. The first once becomes the second and the 
 * second is reused for writing.
 * Using such a technique has the advantage of never blocking the writer, keeping the statistical
 * data consistent and avoid useless copies (if using copy-on write technique).  
 * 
 * @author The ProActive Team
 * @since ProActive Scheduling 1.1
 */
public final class AtomicRMStatisticsHolder {
	
	/**
	 * A reference on statistics accessed only by the writer. 
	 */	
	private RMStatistics writeonlyStatistics;
	
	/**
	 * Atomic reference on statistics accessed by the readers. 
	 */
	private final AtomicReference<RMStatistics> readonlyStatistics;	
	
	/**
	 * Creates a new instance of this class. 
	 */
    public AtomicRMStatisticsHolder(){
    	this.writeonlyStatistics = new RMStatistics();
    	this.readonlyStatistics = new AtomicReference<RMStatistics>(new RMStatistics());
    }
    
    /**
	 * Transmits the incoming node events to the statistics.
	 * @param event incoming event
	 */
	public void nodeEvent(final RMNodeEvent event) {
		this.writeonlyStatistics.nodeEvent(event);
		this.swapAndUpdate();
	}
	
    /**
	 * Transmits the incoming Resource Manager events to the statistics.
	 * @param event incoming event
	 */
	public void rmEvent(final RMEvent event) {
		this.writeonlyStatistics.rmEvent(event);
		this.swapAndUpdate();
	}
    
    /**
     * Atomically swaps in and out, then updates the out.
     */
    private void swapAndUpdate(){
        // First atomically replace read-only by write-only
    	final RMStatistics oldReadonlyStatistics = this.readonlyStatistics.getAndSet(this.writeonlyStatistics);
    	// reuse old and update its values
    	this.writeonlyStatistics = oldReadonlyStatistics.updateFrom(this.writeonlyStatistics);
    }    
    
    /**
     * Returns a consistent view on the statistics if and only if 
     * the caller of this method has called <code>startReadStatistics</code>.
     * @return the resource manager statistics
     */
    public RMStatistics getStatistics(){
    	return this.readonlyStatistics.get();
    }    
}