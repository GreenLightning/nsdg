# NSDG

The Nassi-Shneiderman Diagram Generator is a command line tool which converts xml documents describing the structure of a Nassi-Shneiderman diagram into png files displaying said diagrams.

## Example Diagram

This diagram of the selection sort algorithm has been generated using the xml code below.

![selection-sort](https://cloud.githubusercontent.com/assets/1459067/4448642/52ec9330-4815-11e4-94b7-bd6b2f5048d7.png)

```xml
<diagram title="Selection Sort">
	<loop condition="for i from 0 to n">
		<block>m = i</block>
		<loop condition="for j from i + 1 to n">
			<branch condition="a[j] \l a[m]">
				<left label="yes">
					<block>m = j</block>
				</left>
				<right label="no" />
			</branch>
		</loop>
		<block>
			h = a[m]
			a[m] = a[i]
			a[i] = h
		</block>
	</loop>
</diagram>
```

## Requirements

NSDG requires Java 7 to be properly installed and accessible from the command line.

## Getting Started

The [latest release](https://github.com/GreenLightning/nsdg/releases/latest) contains anything you need to use NSDG. Alongside the generator itself, it contains a user guide in English and in German as well as a bunch of examples including the selection sort algorithm from above. An xml schema is provided, too, which some xml editors can use to validate your document and to propose content. (For more information on that, have a look at the user guide.)

After you have created the xml files, generating the diagrams becomes as easy as this:

    java -jar nsdg.jar <path-to-xml-file>...
