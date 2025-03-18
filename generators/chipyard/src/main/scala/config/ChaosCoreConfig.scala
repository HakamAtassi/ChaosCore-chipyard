package chipyard

import chisel3._

import org.chipsalliance.cde.config.{Config}

import freechips.rocketchip.subsystem._


class ChaosCoreFPGAConfig extends Config(

  new freechips.rocketchip.rocket.WithRV32 ++            // set RocketTiles to be 32-bit


  ////////////////
  // INIT CORES //
  ////////////////

  new ChaosCore.WithNSmallChaosCores(1) ++              // Small = 1 wide, Huge = 4 wide. 

  ////////////////////
  // INIT L2 CACHES //
  ////////////////////

  new freechips.rocketchip.subsystem.WithInclusiveCache(nWays = 2, capacityKB = 64) ++
  new freechips.rocketchip.subsystem.WithNBanks(2) ++


  //////////////////////
  // INIT PERIPHIRALS //
  //////////////////////


  new chipyard.config.WithNoUART() ++       // Dont use SiFive UART
  //new chipyard.config.WithNoDebug() ++      // Dont use top level JTAG Debug IO
  //new testchipip.serdes.WithNoSerialTL ++   // With no output serial TL
  // new freechips.rocketchip.subsystem.WithoutTLMonitors ++ 

  //new freechips.rocketchip.subsystem.WithDefaultMMIOPort() ++             // MPSoC Periphiral domain

  //new chipyard.config.WithUART(address = 0x10020000, baudrate = 115200) ++    
  new freechips.rocketchip.subsystem.WithCustomMMIOPort (base_addr=0x10000000,  // same as default UART
                                                          base_size=0x20000000,      // 
                                                          data_width=64,        //
                                                          maxXferBytes=16,       // FIXME: no clue what this does
                                                          id_bits=4) ++         // MPSoC UART


  new testchipip.boot.WithBootAddrReg(testchipip.boot.BootAddrRegParams(
    defaultBootAddress = 0x40000000L, // This should be DRAM_BASE
    bootRegAddress = 0x1000,
    slaveWhere = PBUS)
  ) ++
  

  //new chipyard.config.WithBootROM(address: BigInt = 0x10000, size: Int = 0x10000, hang: BigInt = 0x10000) ++ 

  /////////////////
  // BASE CONFIG //
  /////////////////

  new chipyard.config.AbstractConfig
)



// BELOW IS THE DEFINITION OF ABSTRACT CONFIG IN CHIPYARD


class ChaosCoreConfig extends Config(

  new freechips.rocketchip.rocket.WithRV32 ++            // set RocketTiles to be 32-bit


  ////////////////
  // INIT CORES //
  ////////////////

  new ChaosCore.WithNHugeChaosCores(1) ++ // Small = 1 wide, Huge = 4 wide. 

  ////////////////////
  // INIT L2 CACHES //
  ////////////////////

  new freechips.rocketchip.subsystem.WithInclusiveCache(nWays = 2, capacityKB = 64) ++
  new freechips.rocketchip.subsystem.WithNBanks(2) ++

  new freechips.rocketchip.subsystem.WithCustomMemPort(base_addr = BigInt("40000000", 16), 
                                                       base_size = BigInt("10000000", 16), 
                                                       data_width = 64, 
                                                       maxXferBytes = 16,
                                                       id_bits = 4) ++


  new testchipip.boot.WithBootAddrReg(testchipip.boot.BootAddrRegParams(
    defaultBootAddress = 0x40000000L, // This should be DRAM_BASE
    bootRegAddress = 0x1000,
    slaveWhere = PBUS)
  ) ++
  //////////////////////
  // INIT PERIPHIRALS //
  //////////////////////

  //new chipyard.config.WithUART(address = 0x10020000, baudrate = 115200) ++    
  //new chipyard.config.WithNoUART() ++ 

  new ChaosCore.WithPPM() ++ 


  /////////////////
  // BASE CONFIG //
  /////////////////

  new chipyard.config.AbstractConfig
)





// BELOW IS THE DEFINITION OF ABSTRACT CONFIG IN CHIPYARD

/*
class AbstractConfig extends Config(
  // ================================================
  //   Set up TestHarness
  // ================================================
  // The HarnessBinders control generation of hardware in the TestHarness
  new chipyard.harness.WithUARTAdapter ++                          /** add UART adapter to display UART on stdout, if uart is present */
  new chipyard.harness.WithBlackBoxSimMem ++                       /** add SimDRAM DRAM model for axi4 backing memory, if axi4 mem is enabled */
  new chipyard.harness.WithSimTSIOverSerialTL ++                   /** add external serial-adapter and RAM */
  new chipyard.harness.WithSimJTAGDebug ++                         /** add SimJTAG if JTAG for debug exposed */
  new chipyard.harness.WithSimDMI ++                               /** add SimJTAG if DMI exposed */
  new chipyard.harness.WithGPIOPinsTiedOff ++                      /** tie-off chiptop GPIO-pins, if GPIO-punchthrough is used */
  new chipyard.harness.WithGPIOTiedOff ++                          /** tie-off chiptop GPIOs, if GPIOs are present */
  new chipyard.harness.WithSimSPIFlashModel ++                     /** add simulated SPI flash memory, if SPI is enabled */
  new chipyard.harness.WithSimAXIMMIO ++                           /** add SimAXIMem for axi4 mmio port, if enabled */
  new chipyard.harness.WithTieOffInterrupts ++                     /** tie-off interrupt ports, if present */
  new chipyard.harness.WithTieOffL2FBusAXI ++                      /** tie-off external AXI4 master, if present */
  new chipyard.harness.WithCustomBootPinPlusArg ++                 /** drive custom-boot pin with a plusarg, if custom-boot-pin is present */
  new chipyard.harness.WithDriveChipIdPin ++                       /** drive chip id pin from harness binder, if chip id pin is present */
  new chipyard.harness.WithSimUARTToUARTTSI ++                     /** connect a SimUART to the UART-TSI port */
  new chipyard.harness.WithClockFromHarness ++                     /** all Clock I/O in ChipTop should be driven by harnessClockInstantiator */
  new chipyard.harness.WithResetFromHarness ++                     /** reset controlled by harness */
  new chipyard.harness.WithAbsoluteFreqHarnessClockInstantiator ++ /** generate clocks in harness with unsynthesizable ClockSourceAtFreqMHz */


  // ================================================
  //   Set up I/O cells + punch I/Os in ChipTop
  // ================================================
  // The IOBinders instantiate ChipTop IOs to match desired digital IOs
  // IOCells are generated for "Chip-like" IOs
  new chipyard.iobinders.WithSerialTLIOCells ++
  new chipyard.iobinders.WithDebugIOCells ++
  new chipyard.iobinders.WithUARTIOCells ++
  new chipyard.iobinders.WithGPIOCells ++
  new chipyard.iobinders.WithSPIFlashIOCells ++
  new chipyard.iobinders.WithExtInterruptIOCells ++
  new chipyard.iobinders.WithChipIdIOCells ++
  new chipyard.iobinders.WithCustomBootPin ++
  new chipyard.iobinders.WithI2CPunchthrough ++
  new chipyard.iobinders.WithSPIIOPunchthrough ++
  new chipyard.iobinders.WithAXI4MemPunchthrough ++
  new chipyard.iobinders.WithAXI4MMIOPunchthrough ++
  new chipyard.iobinders.WithTLMemPunchthrough ++
  new chipyard.iobinders.WithL2FBusAXI4Punchthrough ++
  new chipyard.iobinders.WithBlockDeviceIOPunchthrough ++
  new chipyard.iobinders.WithNICIOPunchthrough ++
  new chipyard.iobinders.WithTraceIOPunchthrough ++
  new chipyard.iobinders.WithUARTTSIPunchthrough ++
  new chipyard.iobinders.WithGCDBusyPunchthrough ++
  new chipyard.iobinders.WithNMITiedOff ++


  // ================================================
  //   Set up External Memory and IO Devices
  // ================================================
  // External memory section
  new testchipip.serdes.WithSerialTL(Seq(                           /** add a serial-tilelink interface */
    testchipip.serdes.SerialTLParams(
      client = Some(testchipip.serdes.SerialTLClientParams(totalIdBits=4)), // serial-tilelink interface will master the FBUS, and support 4 idBits
      phyParams = testchipip.serdes.ExternalSyncSerialPhyParams(phitWidth=32, flitWidth=32) // serial-tilelink interface with 32 lanes
    )
  )) ++
  new freechips.rocketchip.subsystem.WithNMemoryChannels(1) ++         /** Default 1 AXI-4 memory channels */
  new freechips.rocketchip.subsystem.WithNoMMIOPort ++                 /** no top-level MMIO master port (overrides default set in rocketchip) */
  new freechips.rocketchip.subsystem.WithNoSlavePort ++                /** no top-level MMIO slave port (overrides default set in rocketchip) */

  // MMIO device section
  new chipyard.config.WithUART ++                                  /** add a UART */


  // ================================================
  //   Set up Debug/Bringup/Testing Features
  // ================================================
  // JTAG
  new freechips.rocketchip.subsystem.WithDebugSBA ++                /** enable the SBA (system-bus-access) feature of the debug module */
  new chipyard.config.WithDebugModuleAbstractDataWords(8) ++        /** increase debug module data word capacity */
  new freechips.rocketchip.subsystem.WithJtagDTM ++                 /** set the debug module to expose a JTAG port */

  // Boot Select Pins
  new testchipip.boot.WithCustomBootPin ++                          /** add a custom-boot-pin to support pin-driven boot address */
  new testchipip.boot.WithBootAddrReg ++                            /** add a boot-addr-reg for configurable boot address */


  // ================================================
  //   Set up Interrupts
  // ================================================
  // CLINT and PLIC related settings goes here
  new freechips.rocketchip.subsystem.WithNExtTopInterrupts(0) ++    /** no external interrupts */


  // ================================================
  //   Set up Tiles
  // ================================================
  // tile-local settings goes here


  // ================================================
  //   Set up Memory system
  // ================================================
  // On-chip memory section
  new freechips.rocketchip.subsystem.WithDTS("ucb-bar,chipyard", Nil) ++ /** custom device name for DTS (embedded in BootROM) */
  new chipyard.config.WithBootROM ++                                     /** use default bootrom */
  new testchipip.soc.WithMbusScratchpad(base = 0x08000000,               /** add 64 KiB on-chip scratchpad */
                                        size = 64 * 1024) ++

  // Coherency settings
  new freechips.rocketchip.subsystem.WithInclusiveCache ++          /** use Sifive LLC cache as root of coherence */

  // Bus/interconnect settings
  new freechips.rocketchip.subsystem.WithCoherentBusTopology ++     /** hierarchical buses including sbus/mbus/pbus/fbus/cbus/l2 */

  // ================================================
  //   Set up power, reset and clocking
  // ================================================

  // ChipTop clock IO/PLL/Divider/Mux settings
  new chipyard.clocking.WithClockTapIOCells ++                      /** Default generate a clock tapio */
  new chipyard.clocking.WithPassthroughClockGenerator ++

  // DigitalTop-internal clocking settings
  new freechips.rocketchip.subsystem.WithDontDriveBusClocksFromSBus ++  /** leave the bus clocks undriven by sbus */
  new freechips.rocketchip.subsystem.WithClockGateModel ++              /** add default EICG_wrapper clock gate model */
  new chipyard.clocking.WithClockGroupsCombinedByName(("uncore",        /** create a "uncore" clock group tieing all the bus clocks together */
    Seq("sbus", "mbus", "pbus", "fbus", "cbus", "obus", "implicit", "clock_tap"),
    Seq("tile"))) ++

  new chipyard.config.WithPeripheryBusFrequency(500.0) ++           /** Default 500 MHz pbus */
  new chipyard.config.WithMemoryBusFrequency(500.0) ++              /** Default 500 MHz mbus */
  new chipyard.config.WithControlBusFrequency(500.0) ++             /** Default 500 MHz cbus */
  new chipyard.config.WithSystemBusFrequency(500.0) ++              /** Default 500 MHz sbus */
  new chipyard.config.WithFrontBusFrequency(500.0) ++               /** Default 500 MHz fbus */
  new chipyard.config.WithOffchipBusFrequency(500.0) ++             /** Default 500 MHz obus */
  new chipyard.config.WithInheritBusFrequencyAssignments ++         /** Unspecified clocks within a bus will receive the bus frequency if set */
  new chipyard.config.WithNoSubsystemClockIO ++                     /** drive the subsystem diplomatic clocks from ChipTop instead of using implicit clocks */

  // reset

  // power


  // ==================================
  //   Base Settings
  // ==================================
  new freechips.rocketchip.system.BaseConfig                        /** "base" rocketchip system */
)
*/