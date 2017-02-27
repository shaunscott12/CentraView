~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Release Notes for CentraView CBM
CentraView v.2.0.6 on 7/29/2005

Copyright 2003 - 2005 CentraView, LLC  All rights reserved.
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This file contains important information about browser compatibility,
known issues, and other information as appropriate.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DISCLAIMER
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This Readme is provided as a convenience for our customers and partners,
and is subject to, and is not intended to supplement, modify or extend,
any CentraView confidentiality, distributor, and/or End User or other
software license agreements between  CentraView and the customer or
channel partner. Terms and conditions are subject to change. CentraView
is not responsible for errors and omissions in this document for any
reason whatsoever.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Content
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

I.    Browser compatibility
II.   What is not included in this release
III.  What's Different in this release
IV.   Known issues
V.    Deploying this Release
IV.   Contacting CentraView

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
I. Browser compatibility
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

While this version of CentraView minimizes browser dependencies due to
the improved UI design, the following browsers have been the target of
our testing. Other browsers should work well, but Centraview has
limited our testing to the following browsers due to their wide scale
adoption and use in the community.

Internet Explorer 6
 - for Windows 2000 and XP

Mozilla 1.6 and above
 - for Windows 2000, Windows XP,
   RedHat Enterprise and Fedora Core Linux 1 through 4 

Mozilla Firefox 1.0   
 - for Windows 2000, Windows XP,
   RedHat Enterprise and Fedora Core Linux 1 through 4

Netscape 7.1 and above
 - for Windows 2000, Windows XP,
   RedHat Enterprise and Fedora Core Linux 1 through 4 

Opera 7.5x (No Opera 8.x support)
 - for Windows 2000, Windows XP,
   RedHat Enterprise and Fedora Core Linux 1 through 4 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
II. What is not included in this release
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1) QuickBooks Sync
2) LDAP or Active Directory default configurations options
3) User Rights in certain modules

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
III. What's Different in this release
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

- Install
  ~~~~~~~
  We have created a new simplified install, there is a new package
  that you can download and use, and this one has JBoss 4.0.2
  MySQL Connector/J 3.1-nightly build and CentraView packaged together
  The reason may be obvious, the installation will now be much simpler
  You still need to grab MySQL and a Sun JDK on your own and install, 
  but the other steps are simply installing our directory structure, 
  creating the database and telling JBoss how to talk to the database.

- Upgrades
  ~~~~~~~~
  If you were paying close enough attention to the previous paragraph
  you no doubt noticed that we have made CentraView work with a modern
  version of JBoss, JBoss 4.0.2 to be exact, and that has Tomcat5.5
  integrated as well.  We also took the opportunity to update the Struts
  libraries, mostly minor changes.  The downside to this is the newer stuff
  may have trouble with older versions.  The biggest problem there is the
  JSP compiling, so our binary package doesn't have the JSPs precompiled
  mainly because I don't know where you will be installing it.  But they
  should work with any version of JBoss you throw at it.
  
  Also some more problems working with MySQL 4.1 were ironed out.  Still
  the latest connector/j has a bug, at least in the way we are using it
  and I haven't figured out a work around, so you will need to download
  either a nightly connector/j or an older one, I think 3.1.7 didn't have
  this problem.

- Bug Fixes
  ~~~~~~~~~
  - email reply was dropping the subject field
  - email address lookups were broken making it difficult to get addresses
  - custom field list was showing repeats of the same field in administration
  - delegate calendar page had a javascript error, preventing delegation
  - date time popup under opportunities was acting strange
  - the weekly column calendar was giving bad dates when you clicked
    in a box to create an activity
  - The new FAQ screen incorrectly had a permissions button
  - You can now delete custom fields

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
IV. Known issues
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

- Known Issues can be found by looking at our open bugs on
  http://sourceforge.net/projects/centraview

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
V. Deploying This Release
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Deployment is slightly different and easier, if this is your first install
or a fresh install at least, you can download a single package, that
has JBoss, MySQL Connector/J and CentraView within and follow the simple
Installation instructions.

If you already have it running, you probably just need to replace the
centraview.ear, you may want to consider upgrading to a newer JBoss though
because we are, so things on old versions of JBoss may start not working.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
VI. Contacting CentraView
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Please submit all support requests and bug reports on our project
page at http://sourceforge.net/projects/centraview/

We have also launced a centraview-users mailing list, again, this
can be found and subscribed to at our sourceforge page.

(c) 2003-2005 CentraView, LLC.  All rights reserved.

~~~~~~~~~~~~
END OF FILE
~~~~~~~~~~~~
